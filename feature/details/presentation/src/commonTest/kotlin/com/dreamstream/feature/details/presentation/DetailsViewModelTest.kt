package com.dreamstream.feature.details.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.domain.model.DetailContent
import com.dreamstream.feature.details.domain.model.DetailMediaType
import com.dreamstream.feature.details.domain.repository.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeDetailsRepository

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeDetailsRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── init ─────────────────────────────────────────────────────────────────

    @Test
    fun `init loads content detail and updates state`() = runTest {
        fakeRepository.content = fakeDetailContent("t1", "Cosmic Drift")
        val viewModel = viewModel("t1")

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.content).isNotNull()
            val content = checkNotNull(state.content)
            assertThat(content.contentId).isEqualTo("t1")
            assertThat(content.title).isEqualTo("Cosmic Drift")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init sets isLoading false after successful load`() = runTest {
        fakeRepository.content = fakeDetailContent("t1")
        val viewModel = viewModel("t1")

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init sets error state when repository returns NotFound`() = runTest {
        fakeRepository.error = DetailsError.NotFound
        val viewModel = viewModel("unknown")

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.content).isNull()
            assertThat(state.error).isNotNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init sets error state when repository returns LoadFailed`() = runTest {
        fakeRepository.error = DetailsError.LoadFailed
        val viewModel = viewModel("t1")

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.content).isNull()
            assertThat(state.error).isNotNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnRetry ───────────────────────────────────────────────────────────────

    @Test
    fun `OnRetry reloads content after previous error`() = runTest {
        fakeRepository.error = DetailsError.LoadFailed
        val viewModel = viewModel("t1")

        // Consume initial error state
        viewModel.state.test {
            awaitItem()
            cancelAndConsumeRemainingEvents()
        }

        // Fix the repo and retry
        fakeRepository.error = null
        fakeRepository.content = fakeDetailContent("t1", "Cosmic Drift")
        viewModel.onAction(DetailsAction.OnRetry)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.content).isNotNull()
            assertThat(state.error).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnBackClick ───────────────────────────────────────────────────────────

    @Test
    fun `OnBackClick sends NavigateBack event`() = runTest {
        fakeRepository.content = fakeDetailContent("t1")
        val viewModel = viewModel("t1")

        viewModel.events.test {
            viewModel.onAction(DetailsAction.OnBackClick)
            assertThat(awaitItem()).isInstanceOf(DetailsEvent.NavigateBack::class)
            cancelAndConsumeRemainingEvents()
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun viewModel(contentId: String): DetailsViewModel = DetailsViewModel(
        contentId = contentId,
        detailsRepository = fakeRepository,
    )

    private fun fakeDetailContent(
        contentId: String = "t1",
        title: String = "Sample Title",
    ): DetailContent = DetailContent(
        contentId = contentId,
        title = title,
        synopsis = "A sample synopsis.",
        thumbnailUrl = null,
        backdropUrl = null,
        type = DetailMediaType.Movie,
        year = 2024,
        rating = 8.0f,
        genres = listOf("Sci-Fi"),
        durationMinutes = 120,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Fake
// ─────────────────────────────────────────────────────────────────────────────

private class FakeDetailsRepository : DetailsRepository {
    var content: DetailContent? = null
    var error: DetailsError? = null

    override suspend fun getContentDetail(contentId: String): Result<DetailContent, DetailsError> {
        val err = error
        return if (err != null) Result.Error(err)
        else Result.Success(checkNotNull(content) { "FakeDetailsRepository: set content or error before calling" })
    }
}

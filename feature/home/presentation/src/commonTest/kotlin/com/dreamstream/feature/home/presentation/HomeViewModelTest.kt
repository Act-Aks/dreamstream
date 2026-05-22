package com.dreamstream.feature.home.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.Content
import com.dreamstream.feature.home.domain.model.ContentId
import com.dreamstream.feature.home.domain.model.ContentType
import com.dreamstream.feature.home.domain.model.HomeSection
import com.dreamstream.feature.home.domain.repository.HomeRepository
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
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeHomeRepository

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeHomeRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── init ─────────────────────────────────────────────────────────────────

    @Test
    fun `init loads home sections and updates state`() = runTest {
        val sections = listOf(fakeSectionWith("Trending", count = 3))
        fakeRepository.sections = sections

        val viewModel = HomeViewModel(fakeRepository)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.sections).isNotEmpty()
            assertThat(state.sections.first().title).isEqualTo("Trending")
            assertThat(state.sections.first().items).isNotEmpty()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init sets isLoading false after successful load`() = runTest {
        fakeRepository.sections = listOf(fakeSectionWith("A", count = 1))

        val viewModel = HomeViewModel(fakeRepository)

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init emits ShowError event when repository returns error`() = runTest {
        fakeRepository.error = HomeError.LoadFailed

        val viewModel = HomeViewModel(fakeRepository)

        viewModel.events.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(HomeEvent.ShowError::class)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init sets empty sections and no error in state when repository fails`() = runTest {
        fakeRepository.error = HomeError.LoadFailed

        val viewModel = HomeViewModel(fakeRepository)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.sections).isEmpty()
            assertThat(state.error).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnRefresh ─────────────────────────────────────────────────────────────

    @Test
    fun `OnRefresh reloads sections`() = runTest {
        fakeRepository.sections = listOf(fakeSectionWith("Initial", count = 2))
        val viewModel = HomeViewModel(fakeRepository)

        // Consume initial load
        viewModel.state.test {
            awaitItem() // settled state after init
            cancelAndConsumeRemainingEvents()
        }

        // Update the repo and trigger refresh
        fakeRepository.sections = listOf(fakeSectionWith("Refreshed", count = 1))
        viewModel.onAction(HomeAction.OnRefresh)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.sections.first().title).isEqualTo("Refreshed")
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnContentClick ────────────────────────────────────────────────────────

    @Test
    fun `OnContentClick sends NavigateToDetail event with correct id`() = runTest {
        fakeRepository.sections = listOf(fakeSectionWith("A", count = 1))
        val viewModel = HomeViewModel(fakeRepository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnContentClick("content-42"))
            val event = awaitItem()
            assertThat(event).isEqualTo(HomeEvent.NavigateToDetail("content-42"))
            cancelAndConsumeRemainingEvents()
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun fakeSectionWith(title: String, count: Int): HomeSection = HomeSection(
        id = title.lowercase(),
        title = title,
        items = (1..count).map { index ->
            Content(
                id = ContentId("$title-$index"),
                title = "Title $index",
                description = "Desc $index",
                thumbnailUrl = null,
                type = ContentType.Movie,
                year = 2024,
                rating = 8.0f,
            )
        },
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Fake
// ─────────────────────────────────────────────────────────────────────────────

private class FakeHomeRepository : HomeRepository {
    var sections: List<HomeSection> = emptyList()
    var error: HomeError? = null

    override suspend fun getHomeSections(): Result<List<HomeSection>, HomeError> {
        val err = error
        return if (err != null) Result.Error(err) else Result.Success(sections)
    }
}

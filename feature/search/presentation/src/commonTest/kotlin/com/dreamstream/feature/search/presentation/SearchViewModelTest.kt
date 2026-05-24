package com.dreamstream.feature.search.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import androidx.lifecycle.SavedStateHandle
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.feature.search.domain.error.SearchError
import com.dreamstream.feature.search.domain.repository.SearchRepository
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
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeSearchRepository

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeSearchRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── init with blank SavedStateHandle ──────────────────────────────────────

    @Test
    fun `init with no saved query starts with empty state`() = runTest {
        val viewModel = viewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.query).isEqualTo("")
            assertThat(state.results).isEmpty()
            assertThat(state.isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── init with saved query restores results ────────────────────────────────

    @Test
    fun `init with saved query triggers search on restoration`() = runTest {
        fakeRepository.results = fakeResults(count = 2)
        val viewModel = viewModel(savedQuery = "cosmic")

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.query).isEqualTo("cosmic")
            assertThat(state.isLoading).isFalse()
            assertThat(state.results).isNotEmpty()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnQueryChange ─────────────────────────────────────────────────────────

    @Test
    fun `OnQueryChange with non-blank query triggers search and updates results`() = runTest {
        fakeRepository.results = fakeResults(count = 3)
        val viewModel = viewModel()

        viewModel.onAction(SearchAction.OnQueryChange("drift"))

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.query).isEqualTo("drift")
            assertThat(state.isLoading).isFalse()
            assertThat(state.results.size).isEqualTo(3)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `OnQueryChange with blank query clears results`() = runTest {
        fakeRepository.results = fakeResults(count = 2)
        val viewModel = viewModel()

        viewModel.onAction(SearchAction.OnQueryChange("something"))
        viewModel.onAction(SearchAction.OnQueryChange(""))

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.query).isEqualTo("")
            assertThat(state.results).isEmpty()
            assertThat(state.isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `OnQueryChange sets isLoading false after successful search`() = runTest {
        fakeRepository.results = fakeResults(count = 1)
        val viewModel = viewModel()

        viewModel.onAction(SearchAction.OnQueryChange("x"))

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `OnQueryChange sets error in state when repository returns error`() = runTest {
        fakeRepository.error = SearchError.SearchFailed
        val viewModel = viewModel()

        viewModel.onAction(SearchAction.OnQueryChange("x"))

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.results).isEmpty()
            assertThat(state.error).isNotNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnClearQuery ──────────────────────────────────────────────────────────

    @Test
    fun `OnClearQuery resets query and clears results`() = runTest {
        fakeRepository.results = fakeResults(count = 2)
        val viewModel = viewModel()

        viewModel.onAction(SearchAction.OnQueryChange("cosmic"))

        viewModel.state.test {
            awaitItem() // consumed results state
            cancelAndConsumeRemainingEvents()
        }

        viewModel.onAction(SearchAction.OnClearQuery)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.query).isEqualTo("")
            assertThat(state.results).isEmpty()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnResultClick ─────────────────────────────────────────────────────────

    @Test
    fun `OnResultClick sends NavigateToDetail event with correct contentId`() = runTest {
        val viewModel = viewModel()

        viewModel.events.test {
            viewModel.onAction(SearchAction.OnResultClick("t1"))
            assertThat(awaitItem()).isEqualTo(SearchEvent.NavigateToDetail("t1"))
            cancelAndConsumeRemainingEvents()
        }
    }

    // ── OnBackClick ───────────────────────────────────────────────────────────

    @Test
    fun `OnBackClick sends NavigateBack event`() = runTest {
        val viewModel = viewModel()

        viewModel.events.test {
            viewModel.onAction(SearchAction.OnBackClick)
            assertThat(awaitItem()).isInstanceOf(SearchEvent.NavigateBack::class)
            cancelAndConsumeRemainingEvents()
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun viewModel(savedQuery: String = ""): SearchViewModel = SearchViewModel(
        savedStateHandle = SavedStateHandle(if (savedQuery.isNotEmpty()) mapOf("query" to savedQuery) else emptyMap()),
        searchRepository = fakeRepository,
    )

    private fun fakeResults(count: Int): List<SearchResult> = (1..count).map { i ->
        MovieResult(
            name = "Result $i",
            url = "r$i",
            providerId = "test",
            year = 2024,
            rating = 8.0f,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Fake
// ─────────────────────────────────────────────────────────────────────────────

private class FakeSearchRepository : SearchRepository {
    var results: List<SearchResult> = emptyList()
    var error: SearchError? = null

    override suspend fun search(query: String): Result<List<SearchResult>, SearchError> {
        val err = error
        return if (err != null) Result.Error(err)
        else Result.Success(results)
    }
}

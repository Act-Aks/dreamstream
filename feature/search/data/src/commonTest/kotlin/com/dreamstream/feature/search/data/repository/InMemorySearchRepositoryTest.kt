package com.dreamstream.feature.search.data.repository

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.domain.model.search.AnimeResult
import com.dreamstream.core.domain.model.search.MovieResult
import com.dreamstream.core.domain.model.search.SeriesResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class InMemorySearchRepositoryTest {

    private val repository = InMemorySearchRepository()

    // ── Blank / empty query ───────────────────────────────────────────────────

    @Test
    fun `search with blank query returns empty list`() = runTest {
        val result = repository.search("") as Result.Success
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `search with whitespace-only query returns empty list`() = runTest {
        val result = repository.search("   ") as Result.Success
        assertThat(result.data).isEmpty()
    }

    // ── Exact title matches ───────────────────────────────────────────────────

    @Test
    fun `search for exact title returns single match`() = runTest {
        val result = repository.search("Cosmic Drift") as Result.Success
        assertThat(result.data).isNotEmpty()
        assertThat(result.data.first().url).isEqualTo("t1")
        assertThat(result.data.first().name).isEqualTo("Cosmic Drift")
    }

    @Test
    fun `search is case insensitive`() = runTest {
        val lower = (repository.search("cosmic drift") as Result.Success).data
        val upper = (repository.search("COSMIC DRIFT") as Result.Success).data
        assertThat(lower).isEqualTo(upper)
        assertThat(lower).isNotEmpty()
    }

    // ── Partial matches ───────────────────────────────────────────────────────

    @Test
    fun `search returns multiple results for shared substring`() = runTest {
        // "The Last Horizon" and potentially others containing common words
        val result = (repository.search("Horizon") as Result.Success).data
        assertThat(result).isNotEmpty()
        assertThat(result.any { it.url == "t3" }).isEqualTo(true)
    }

    @Test
    fun `search prefix matches correctly`() = runTest {
        val result = (repository.search("Neon") as Result.Success).data
        assertThat(result).isNotEmpty()
        assertThat(result.first().url).isEqualTo("t2")
    }

    // ── No match ──────────────────────────────────────────────────────────────

    @Test
    fun `search returns empty list when no items match`() = runTest {
        val result = (repository.search("xyzzy-no-match-ever") as Result.Success).data
        assertThat(result).isEmpty()
    }

    // ── Result types ──────────────────────────────────────────────────────────

    @Test
    fun `search returns MovieResult for movie entries`() = runTest {
        val result = (repository.search("Cosmic Drift") as Result.Success).data
        assertThat(result.first()).isInstanceOf(MovieResult::class)
    }

    @Test
    fun `search returns SeriesResult for series entries`() = runTest {
        val result = (repository.search("Neon Shadows") as Result.Success).data
        assertThat(result.first()).isInstanceOf(SeriesResult::class)
    }

    @Test
    fun `search returns AnimeResult for anime entries`() = runTest {
        val result = (repository.search("Sakura") as Result.Success).data
        assertThat(result.first()).isInstanceOf(AnimeResult::class)
    }

    // ── Stability ─────────────────────────────────────────────────────────────

    @Test
    fun `successive identical searches return equal results`() = runTest {
        val first = (repository.search("Protocol") as Result.Success).data
        val second = (repository.search("Protocol") as Result.Success).data
        assertThat(first).isEqualTo(second)
    }

    // ── All 10 catalog items are searchable ───────────────────────────────────

    @Test
    fun `all ten stub titles are reachable by exact search`() = runTest {
        val titles = listOf(
            "Cosmic Drift", "Neon Shadows", "The Last Horizon", "Sakura Rising",
            "Iron Meridian", "Pulse", "Echoes of Tomorrow",
            "Midnight Anthology", "Void Protocol", "Blue Frontier",
        )
        titles.forEach { title ->
            val results = (repository.search(title) as Result.Success).data
            assertThat(results.any { it.name == title }).isEqualTo(true)
        }
    }
}

package com.dreamstream.feature.home.data.repository

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class InMemoryHomeRepositoryTest {

    private val repository = InMemoryHomeRepository()

    // ── Success shape ─────────────────────────────────────────────────────────

    @Test
    fun `getHomeSections returns Success`() = runTest {
        val result = repository.getHomeSections()
        assertThat(result).isInstanceOf(Result.Success::class)
    }

    @Test
    fun `getHomeSections returns three sections`() = runTest {
        val result = repository.getHomeSections() as Result.Success
        assertThat(result.data).hasSize(3)
    }

    // ── Section identities ────────────────────────────────────────────────────

    @Test
    fun `first section is Trending Now`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        assertThat(sections[0].id).isEqualTo("trending")
        assertThat(sections[0].title).isEqualTo("Trending Now")
    }

    @Test
    fun `second section is New Releases`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        assertThat(sections[1].id).isEqualTo("new_releases")
        assertThat(sections[1].title).isEqualTo("New Releases")
    }

    @Test
    fun `third section is Top Rated`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        assertThat(sections[2].id).isEqualTo("top_rated")
        assertThat(sections[2].title).isEqualTo("Top Rated")
    }

    // ── Item counts ───────────────────────────────────────────────────────────

    @Test
    fun `Trending Now section has four items`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        assertThat(sections[0].items).hasSize(4)
    }

    @Test
    fun `New Releases section has three items`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        assertThat(sections[1].items).hasSize(3)
    }

    @Test
    fun `Top Rated section has three items`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        assertThat(sections[2].items).hasSize(3)
    }

    // ── Item integrity ────────────────────────────────────────────────────────

    @Test
    fun `each item has a non-empty title`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        sections.flatMap { it.items }.forEach { content ->
            assertThat(content.title).isNotEmpty()
        }
    }

    @Test
    fun `each item has a non-empty id`() = runTest {
        val sections = (repository.getHomeSections() as Result.Success).data
        sections.flatMap { it.items }.forEach { content ->
            assertThat(content.id.value).isNotEmpty()
        }
    }

    // ── Stability ─────────────────────────────────────────────────────────────

    @Test
    fun `successive calls return equal section lists`() = runTest {
        val first = (repository.getHomeSections() as Result.Success).data
        val second = (repository.getHomeSections() as Result.Success).data
        assertThat(first).isEqualTo(second)
    }
}

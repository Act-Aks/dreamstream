package com.dreamstream.feature.details.data.repository

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.details.domain.error.DetailsError
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class InMemoryDetailsRepositoryTest {

    private val repository = InMemoryDetailsRepository()

    // ── Known IDs return Success ───────────────────────────────────────────────

    @Test
    fun `getContentDetail returns Success for known id t1`() = runTest {
        assertThat(repository.getContentDetail("t1")).isInstanceOf(Result.Success::class)
    }

    @Test
    fun `getContentDetail returns correct title for t1`() = runTest {
        val result = repository.getContentDetail("t1") as Result.Success
        assertThat(result.data.title).isEqualTo("Cosmic Drift")
        assertThat(result.data.contentId).isEqualTo("t1")
    }

    @Test
    fun `getContentDetail returns Success for all ten known ids`() = runTest {
        val knownIds = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownIds.forEach { id ->
            val result = repository.getContentDetail(id)
            assertThat(result).isInstanceOf(Result.Success::class)
        }
    }

    // ── Unknown IDs return NotFound ────────────────────────────────────────────

    @Test
    fun `getContentDetail returns NotFound for unknown id`() = runTest {
        val result = repository.getContentDetail("does-not-exist") as Result.Error
        assertThat(result.error).isEqualTo(DetailsError.NotFound)
    }

    @Test
    fun `getContentDetail returns NotFound for empty string id`() = runTest {
        val result = repository.getContentDetail("") as Result.Error
        assertThat(result.error).isEqualTo(DetailsError.NotFound)
    }

    // ── Content integrity ──────────────────────────────────────────────────────

    @Test
    fun `each known content has a non-empty synopsis`() = runTest {
        val knownIds = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownIds.forEach { id ->
            val content = (repository.getContentDetail(id) as Result.Success).data
            assertThat(content.synopsis).isNotEmpty()
        }
    }

    @Test
    fun `each known content has a non-empty genres list`() = runTest {
        val knownIds = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownIds.forEach { id ->
            val content = (repository.getContentDetail(id) as Result.Success).data
            assertThat(content.genres).isNotEmpty()
        }
    }

    @Test
    fun `each known content has a non-null type`() = runTest {
        val knownIds = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownIds.forEach { id ->
            val content = (repository.getContentDetail(id) as Result.Success).data
            assertThat(content.type).isNotNull()
        }
    }

    // ── Stability ──────────────────────────────────────────────────────────────

    @Test
    fun `successive calls for same id return equal content`() = runTest {
        val first = (repository.getContentDetail("r2") as Result.Success).data
        val second = (repository.getContentDetail("r2") as Result.Success).data
        assertThat(first).isEqualTo(second)
    }
}

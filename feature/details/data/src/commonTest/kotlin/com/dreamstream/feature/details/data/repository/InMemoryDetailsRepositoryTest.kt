package com.dreamstream.feature.details.data.repository

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.detail.ContentDetail
import com.dreamstream.feature.details.domain.error.DetailsError
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class InMemoryDetailsRepositoryTest {

    private val repository = InMemoryDetailsRepository()

    // ── Known URLs return Success ──────────────────────────────────────────────

    @Test
    fun `getContentDetail returns Success for known url t1`() = runTest {
        assertThat(repository.getContentDetail("t1")).isInstanceOf(Result.Success::class)
    }

    @Test
    fun `getContentDetail returns correct name and url for t1`() = runTest {
        val result = repository.getContentDetail("t1") as Result.Success
        assertThat(result.data.name).isEqualTo("Cosmic Drift")
        assertThat(result.data.url).isEqualTo("t1")
    }

    @Test
    fun `getContentDetail returns Success for all ten known urls`() = runTest {
        val knownUrls = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownUrls.forEach { url ->
            val result = repository.getContentDetail(url)
            assertThat(result).isInstanceOf(Result.Success::class)
        }
    }

    // ── Unknown URLs return NotFound ───────────────────────────────────────────

    @Test
    fun `getContentDetail returns NotFound for unknown url`() = runTest {
        val result = repository.getContentDetail("does-not-exist") as Result.Error
        assertThat(result.error).isEqualTo(DetailsError.NotFound)
    }

    @Test
    fun `getContentDetail returns NotFound for empty string url`() = runTest {
        val result = repository.getContentDetail("") as Result.Error
        assertThat(result.error).isEqualTo(DetailsError.NotFound)
    }

    // ── Content integrity ──────────────────────────────────────────────────────

    @Test
    fun `each known content has a non-empty plot`() = runTest {
        val knownUrls = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownUrls.forEach { url ->
            val content = (repository.getContentDetail(url) as Result.Success).data
            assertThat(content.plot).isNotNull()
            assertThat(content.plot!!).isNotEmpty()
        }
    }

    @Test
    fun `each known content has a non-empty tags list`() = runTest {
        val knownUrls = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownUrls.forEach { url ->
            val content = (repository.getContentDetail(url) as Result.Success).data
            assertThat(content.tags).isNotEmpty()
        }
    }

    @Test
    fun `each known content has a non-null type`() = runTest {
        val knownUrls = listOf("t1", "t2", "t3", "t4", "n1", "n2", "n3", "r1", "r2", "r3")
        knownUrls.forEach { url ->
            val content = (repository.getContentDetail(url) as Result.Success).data
            assertThat(content.type).isNotNull()
        }
    }

    // ── Stability ──────────────────────────────────────────────────────────────

    @Test
    fun `successive calls for same url return equal content`() = runTest {
        val first = (repository.getContentDetail("r2") as Result.Success).data
        val second = (repository.getContentDetail("r2") as Result.Success).data
        assertThat(first).isEqualTo(second)
    }
}

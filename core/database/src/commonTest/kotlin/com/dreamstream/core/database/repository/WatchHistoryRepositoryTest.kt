package com.dreamstream.core.database.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.dreamstream.core.database.TestDatabaseHolder
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.user.WatchHistory
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Take too much time")
class WatchHistoryRepositoryTest {
    private val repository =
        WatchHistoryRepository(TestDatabaseHolder.getSharedDatabase().watchHistoryDao())

    @Test
    fun testInsertAndObserveAll() = runTest {
        val entry = createWatchHistoryEntry()

        repository.insert(entry)

        repository.observeAll().test {
            val list = awaitItem()
            assertThat(list).hasSize(1)
            assertThat(list.first().title).isEqualTo("Test Movie")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUpdateProgress() = runTest {
        val entry = createWatchHistoryEntry()
        repository.insert(entry)

        repository.updateProgress("1", 5000L, 10000L, 1234567891L)

        val updated = repository.getById("1")
        assertThat(updated).isNotNull()
        assertThat(updated?.watchPositionMs).isEqualTo(5000L)
        assertThat(updated?.totalDurationMs).isEqualTo(10000L)
    }

    @Test
    fun testGetByUrl() = runTest {
        val entry = createWatchHistoryEntry()
        repository.insert(entry)

        val read = repository.getByUrl("https://example.com", "provider1")
        assertThat(read).isNotNull()
        assertThat(read?.id).isEqualTo("1")
    }

    @Test
    fun testDeleteById() = runTest {
        val entry = createWatchHistoryEntry()
        repository.insert(entry)

        repository.deleteById("1")

        val deleted = repository.getById("1")
        assertThat(deleted).isNull()
    }

    private fun createWatchHistoryEntry() = WatchHistory(
        id = "1",
        providerId = "provider1",
        url = "https://example.com",
        title = "Test Movie",
        posterUrl = "https://example.com/poster.jpg",
        type = ContentType.Movie,
        episodeName = null,
        season = null,
        episode = null,
        episodeData = null,
        watchPositionMs = 0L,
        totalDurationMs = 10000L,
        lastWatchedAt = 1234567890L,
        createdAt = 1234567890L
    )
}

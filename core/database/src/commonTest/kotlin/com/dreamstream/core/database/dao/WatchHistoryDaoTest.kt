package com.dreamstream.core.database.dao

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.dreamstream.core.database.TestDatabaseHolder
import com.dreamstream.core.database.entity.WatchHistoryEntity
import com.dreamstream.core.model.catalog.ContentType
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Take too much time")
class WatchHistoryDaoTest {
    private val dao = TestDatabaseHolder.getSharedDatabase().watchHistoryDao()

    @Test
    fun testInsertAndRead() = runTest {
        val entity = createWatchHistoryEntity("1")

        dao.insert(entity)

        val read = dao.getById("1")
        assertThat(read).isNotNull()
        assertThat(read?.id).isEqualTo("1")
        assertThat(read?.title).isEqualTo("Test Movie")
        assertThat(read?.contentType).isEqualTo(ContentType.Movie)
    }

    @Test
    fun testUpdateProgress() = runTest {
        val entity = createWatchHistoryEntity("1")
        dao.insert(entity)

        dao.updateProgress("1", 5000L, 10000L, 1234567890L)

        val updated = dao.getById("1")
        assertThat(updated).isNotNull()
        assertThat(updated?.watchPositionMs).isEqualTo(5000L)
        assertThat(updated?.totalDurationMs).isEqualTo(10000L)
        assertThat(updated?.lastWatchedAt).isEqualTo(1234567890L)
    }

    @Test
    fun testDeleteById() = runTest {
        val entity = createWatchHistoryEntity("1")
        dao.insert(entity)

        dao.deleteById("1")

        val deleted = dao.getById("1")
        assertThat(deleted).isNull()
    }

    @Test
    fun testGetByUrl() = runTest {
        val entity = createWatchHistoryEntity("1")
        dao.insert(entity)

        val read = dao.getByUrl("https://example.com", "provider1")
        assertThat(read).isNotNull()
        assertThat(read?.id).isEqualTo("1")
    }

    @Test
    fun testGetByUrlNotFound() = runTest {
        val result = dao.getByUrl("https://notfound.com", "provider1")
        assertThat(result).isNull()
    }

    @Test
    fun testCountAll() = runTest {
        dao.insert(createWatchHistoryEntity("1"))
        dao.insert(createWatchHistoryEntity("2"))
        dao.insert(createWatchHistoryEntity("3"))

        val count = dao.countAll()
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun testDeleteAll() = runTest {
        dao.insert(createWatchHistoryEntity("1"))
        dao.insert(createWatchHistoryEntity("2"))

        dao.deleteAll()

        val count = dao.countAll()
        assertThat(count).isEqualTo(0)
    }

    private fun createWatchHistoryEntity(id: String) = WatchHistoryEntity(
        id = id,
        providerId = "provider1",
        url = "https://example.com",
        title = "Test Movie",
        posterUrl = "https://example.com/poster.jpg",
        contentType = ContentType.Movie,
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

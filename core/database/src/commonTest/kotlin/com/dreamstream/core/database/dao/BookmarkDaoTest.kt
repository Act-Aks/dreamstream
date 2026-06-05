package com.dreamstream.core.database.dao

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.dreamstream.core.database.TestDatabaseHolder
import com.dreamstream.core.database.entity.BookmarkEntity
import com.dreamstream.core.domain.model.catalog.ContentType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Take too much time")
class BookmarkDaoTest {
    private val dao = TestDatabaseHolder.getSharedDatabase().bookmarkDao()

    @Test
    fun testInsertAndIsBookmarked() = runTest {
        val entity = createBookmarkEntity("1")

        dao.insert(entity)

        val isBookmarked = dao.isBookmarked("https://example.com", "provider1")
        assertThat(isBookmarked).isTrue()
    }

    @Test
    fun testIsBookmarkedNotFound() = runTest {
        val isBookmarked = dao.isBookmarked("https://notfound.com", "provider1")
        assertThat(isBookmarked).isFalse()
    }

    @Test
    fun testInsertDuplicateIgnores() = runTest {
        val entity = createBookmarkEntity("1")

        dao.insert(entity)
        dao.insert(entity) // Duplicate

        val all = dao.observeAll().first()
        assertThat(all).hasSize(1)
    }

    @Test
    fun testUpdateCategory() = runTest {
        val entity = createBookmarkEntity("1")
        dao.insert(entity)

        dao.updateCategory("1", "Favorites")

        val all = dao.observeAll().first()
        assertThat(all).hasSize(1)
        assertThat(all.first().category).isEqualTo("Favorites")
    }

    @Test
    fun testObserveByCategory() = runTest {
        dao.insert(createBookmarkEntity("1", "Watchlist"))
        dao.insert(createBookmarkEntity("2", "Watchlist"))
        dao.insert(createBookmarkEntity("3", "Favorites"))

        val watchlist = dao.observeByCategory("Watchlist").first()
        assertThat(watchlist).hasSize(2)

        val favorites = dao.observeByCategory("Favorites").first()
        assertThat(favorites).hasSize(1)
    }

    @Test
    fun testCountByCategory() = runTest {
        dao.insert(createBookmarkEntity("1", "Watchlist"))
        dao.insert(createBookmarkEntity("2", "Watchlist"))
        dao.insert(createBookmarkEntity("3", "Favorites"))

        val counts = dao.countByCategory()
        val watchlistCount = counts.find { it.category == "Watchlist" }?.count
        val favoritesCount = counts.find { it.category == "Favorites" }?.count

        assertThat(watchlistCount).isEqualTo(2)
        assertThat(favoritesCount).isEqualTo(1)
    }

    @Test
    fun testGetByUrl() = runTest {
        val entity = createBookmarkEntity("1")
        dao.insert(entity)

        val read = dao.getByUrl("https://example.com", "provider1")
        assertThat(read).isNotNull()
        assertThat(read?.id).isEqualTo("1")
    }

    @Test
    fun testDeleteById() = runTest {
        val entity = createBookmarkEntity("1")
        dao.insert(entity)

        dao.deleteById("1")

        val all = dao.observeAll().first()
        assertThat(all).isEmpty()
    }

    private fun createBookmarkEntity(id: String, category: String = "Watchlist") = BookmarkEntity(
        id = id,
        providerId = "provider1",
        url = "https://example.com",
        title = "Test Title",
        posterUrl = "https://example.com/poster.jpg",
        contentType = ContentType.Movie,
        category = category,
        createdAt = 1234567890L
    )
}

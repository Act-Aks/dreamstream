package com.dreamstream.core.database.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.dreamstream.core.database.TestDatabaseHolder
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.user.Bookmark
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Take too much time")
class BookmarkRepositoryTest {
    private val repository =
        BookmarkRepository(TestDatabaseHolder.getSharedDatabase().bookmarkDao())

    @Test
    fun testInsertAndIsBookmarked() = runTest {
        val bookmark = createBookmark("1")

        repository.insert(bookmark)

        val isBookmarked = repository.isBookmarked("https://example.com", "provider1")
        assertThat(isBookmarked).isTrue()
    }

    @Test
    fun testObserveAll() = runTest {
        repository.insert(createBookmark("1"))
        repository.insert(createBookmark("2"))

        repository.observeAll().test {
            val list = awaitItem()
            assertThat(list).hasSize(2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testObserveByCategory() = runTest {
        repository.insert(createBookmark("1", "Watchlist"))
        repository.insert(createBookmark("2", "Watchlist"))
        repository.insert(createBookmark("3", "Favorites"))

        repository.observeByCategory("Watchlist").test {
            val list = awaitItem()
            assertThat(list).hasSize(2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUpdateCategory() = runTest {
        val bookmark = createBookmark("1")
        repository.insert(bookmark)

        repository.updateCategory("1", "Favorites")

        val updated = repository.observeAll().first().first()
        assertThat(updated.category).isEqualTo("Favorites")
    }

    @Test
    fun testInsertOrUpdate() = runTest {
        val bookmark = createBookmark("1")

        repository.insertOrUpdate(bookmark)  // Insert
        repository.insertOrUpdate(bookmark)  // Update category

        val all = repository.observeAll().first()
        assertThat(all).hasSize(1)
    }

    @Test
    fun testDeleteById() = runTest {
        val bookmark = createBookmark("1")
        repository.insert(bookmark)

        repository.deleteById("1")

        val all = repository.observeAll().first()
        assertThat(all).isEmpty()
    }

    private fun createBookmark(
        id: String,
        category: String = "Watchlist"
    ) = Bookmark(
        id = id,
        providerId = "provider1",
        url = "https://example.com",
        title = "Test Title",
        posterUrl = "https://example.com/poster.jpg",
        type = ContentType.Movie,
        category = category,
        createdAt = 1234567890L
    )
}

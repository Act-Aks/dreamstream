package com.dreamstream.core.database.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.dreamstream.core.database.TestDatabaseHolder
import com.dreamstream.core.domain.model.plugin.PluginRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Take too much time")
class RepositoryRepositoryTest {
    private val repository =
        RepositoryRepository(TestDatabaseHolder.getSharedDatabase().repositoryDao())

    @Test
    fun testInsertAndRead() = runTest {
        val repo = createRepositoryModel(REPO_BASE)

        repository.insert(repo)

        val read = repository.getByUrl(REPO_BASE)
        assertThat(read).isNotNull()
        assertThat(read?.name).isEqualTo("Official Repository")
        assertThat(read?.url).isEqualTo(REPO_BASE)
    }

    @Test
    fun testExists() = runTest {
        val repo = createRepositoryModel(REPO_BASE)
        repository.insert(repo)

        val exists = repository.exists(REPO_BASE)
        assertThat(exists).isTrue()

        val notExists = repository.exists("https://notfound.com/manifest.json")
        assertThat(notExists).isFalse()
    }

    @Test
    fun testObserveAll() = runTest {
        repository.insert(createRepositoryModel(REPO_ONE))
        repository.insert(createRepositoryModel(REPO_TWO))

        repository.observeAll().test {
            val list = awaitItem()
            assertThat(list).hasSize(2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testObserveEnabled() = runTest {
        repository.insert(
            createRepositoryModel(REPO_ONE, isEnabled = true)
        )
        repository.insert(
            createRepositoryModel(REPO_TWO, isEnabled = false)
        )

        repository.observeEnabled().test {
            val list = awaitItem()
            assertThat(list).hasSize(1)
            assertThat(list.first().name).isEqualTo("Official Repository")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testSetEnabled() = runTest {
        val repo = createRepositoryModel(REPO_BASE, isEnabled = true)
        repository.insert(repo)

        repository.setEnabled(REPO_BASE, false)

        val updated = repository.getByUrl(REPO_BASE)
        assertThat(updated).isNotNull()
        assertThat(updated?.isEnabled).isEqualTo(false)
    }

    @Test
    fun testUpdateLastFetched() = runTest {
        val repo = createRepositoryModel(REPO_BASE)
        repository.insert(repo)

        repository.updateLastFetched(
            url = REPO_BASE,
            lastFetched = 1234567891L,
            name = "Updated Name",
            description = "Updated description"
        )

        val updated = repository.getByUrl(REPO_BASE)
        assertThat(updated).isNotNull()
        assertThat(updated?.lastFetched).isEqualTo(1234567891L)
        assertThat(updated?.name).isEqualTo("Updated Name")
        assertThat(updated?.description).isEqualTo("Updated description")
    }

    @Test
    fun testDeleteByUrl() = runTest {
        val repo = createRepositoryModel(REPO_BASE)
        repository.insert(repo)

        repository.deleteByUrl(REPO_BASE)

        val deleted = repository.getByUrl(REPO_BASE)
        assertThat(deleted).isNull()
    }

    @Test
    fun testDeleteAll() = runTest {
        repository.insert(createRepositoryModel(REPO_ONE))
        repository.insert(createRepositoryModel(REPO_TWO))

        repository.deleteAll()

        val all = repository.observeAll().first()
        assertThat(all).isEmpty()
    }

    private fun createRepositoryModel(
        url: String, isEnabled: Boolean = true
    ) = PluginRepository(
        url = url,
        name = "Official Repository",
        description = "Official plugin repository",
        manifestVersion = 1,
        isEnabled = isEnabled,
        lastFetched = 1234567890L,
        addedAt = 1234567890L
    )

    companion object {
        private const val REPO_BASE = "https://repo.example.com/manifest.json"
        private const val REPO_ONE = "https://repo1.com/manifest.json"
        private const val REPO_TWO = "https://repo2.com/manifest.json"
    }
}

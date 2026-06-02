package com.dreamstream.core.database.dao

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.dreamstream.core.database.TestDatabaseFactory
import com.dreamstream.core.database.entity.RepositoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RepositoryDaoTest {
    private val database = TestDatabaseFactory.createTestDatabase()
    private val dao = database.repositoryDao()

    @Test
    fun testInsertAndRead() = runTest {
        val entity = createRepositoryEntity(REPO_BASE)

        dao.insert(entity)

        val read = dao.getByUrl(REPO_BASE)
        assertThat(read).isNotNull()
        assertThat(read?.name).isEqualTo("Official Repository")
    }

    @Test
    fun testExists() = runTest {
        val entity = createRepositoryEntity(REPO_BASE)
        dao.insert(entity)

        val exists = dao.exists(REPO_BASE)
        assertThat(exists).isTrue()

        val notExists = dao.exists("https://notfound.com/manifest.json")
        assertThat(notExists).isFalse()
    }

    @Test
    fun testObserveAll() = runTest {
        dao.insert(createRepositoryEntity(REPO_ONE))
        dao.insert(createRepositoryEntity(REPO_TWO))

        val all = dao.observeAll().first()
        assertThat(all).hasSize(2)
    }

    @Test
    fun testObserveEnabled() = runTest {
        dao.insert(createRepositoryEntity(REPO_ONE, isEnabled = true))
        dao.insert(createRepositoryEntity(REPO_TWO, isEnabled = false))

        val enabled = dao.observeEnabled().first()
        assertThat(enabled).hasSize(1)
    }

    @Test
    fun testSetEnabled() = runTest {
        val entity = createRepositoryEntity(REPO_BASE)
        dao.insert(entity)

        dao.setEnabled(REPO_BASE, false)

        val updated = dao.getByUrl(REPO_BASE)
        assertThat(updated).isNotNull()
        assertThat(updated?.isEnabled).isEqualTo(false)
    }

    @Test
    fun testUpdateLastFetched() = runTest {
        val entity = createRepositoryEntity(REPO_BASE)
        dao.insert(entity)

        dao.updateLastFetched(
            lastFetched = 1234567891L,
            name = "Updated Name",
            description = "Updated description",
            url = REPO_BASE
        )

        val updated = dao.getByUrl(REPO_BASE)
        assertThat(updated).isNotNull()
        assertThat(updated?.lastFetched).isEqualTo(1234567891L)
        assertThat(updated?.name).isEqualTo("Updated Name")
        assertThat(updated?.description).isEqualTo("Updated description")
    }

    @Test
    fun testDeleteByUrl() = runTest {
        val entity = createRepositoryEntity(REPO_BASE)
        dao.insert(entity)

        dao.deleteByUrl(REPO_BASE)

        val deleted = dao.getByUrl(REPO_BASE)
        assertThat(deleted).isNull()
    }

    @Test
    fun testDeleteAll() = runTest {
        dao.insert(createRepositoryEntity(REPO_ONE))
        dao.insert(createRepositoryEntity(REPO_TWO))

        dao.deleteAll()

        val all = dao.observeAll().first()
        assertThat(all).isEmpty()
    }

    private fun createRepositoryEntity(
        url: String, isEnabled: Boolean = true
    ) = RepositoryEntity(
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

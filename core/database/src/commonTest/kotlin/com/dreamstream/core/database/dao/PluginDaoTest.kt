package com.dreamstream.core.database.dao

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.dreamstream.core.database.TestDatabaseFactory
import com.dreamstream.core.database.entity.InstalledPluginEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class PluginDaoTest {
    private val database = TestDatabaseFactory.createTestDatabase()
    private val dao = database.pluginDao()

    @Test
    fun testInsertAndRead() = runTest {
        val entity = createPluginEntity("1")

        dao.insert(entity)

        val read = dao.getById("1")
        assertThat(read).isNotNull()
        assertThat(read?.name).isEqualTo("Anime Provider")
        assertThat(read?.version).isEqualTo(1)
    }

    @Test
    fun testObserveAll() = runTest {
        dao.insert(createPluginEntity("1"))
        dao.insert(createPluginEntity("2"))

        val all = dao.observeAll().first()
        assertThat(all).hasSize(2)
    }

    @Test
    fun testObserveEnabled() = runTest {
        dao.insert(createPluginEntity("1", isEnabled = true))
        dao.insert(createPluginEntity("2", isEnabled = false))

        val enabled = dao.observeEnabled().first()
        assertThat(enabled).hasSize(1)
        assertThat(enabled.first().name).isEqualTo("Anime Provider")
    }

    @Test
    fun testSetEnabled() = runTest {
        val entity = createPluginEntity("1", isEnabled = true)
        dao.insert(entity)

        dao.setEnabled("1", false)

        val updated = dao.getById("1")
        assertThat(updated).isNotNull()
        assertThat(updated?.isEnabled).isEqualTo(false)
    }

    @Test
    fun testUpdateVersion() = runTest {
        val entity = createPluginEntity("1")
        dao.insert(entity)

        dao.updateVersion("1", 2, "2.0.0", "/new/path.jar", 1234567891L)

        val updated = dao.getById("1")
        assertThat(updated).isNotNull()
        assertThat(updated?.version).isEqualTo(2)
        assertThat(updated?.versionName).isEqualTo("2.0.0")
        assertThat(updated?.filePath).isEqualTo("/new/path.jar")
        assertThat(updated?.updatedAt).isEqualTo(1234567891L)
    }

    @Test
    fun testDeleteById() = runTest {
        val entity = createPluginEntity("1")
        dao.insert(entity)

        dao.deleteById("1")

        val deleted = dao.getById("1")
        assertThat(deleted).isNull()
    }

    @Test
    fun testCountAll() = runTest {
        dao.insert(createPluginEntity("1"))
        dao.insert(createPluginEntity("2"))
        dao.insert(createPluginEntity("3"))

        val count = dao.countAll()
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun testDeleteAll() = runTest {
        dao.insert(createPluginEntity("1"))
        dao.insert(createPluginEntity("2"))

        dao.deleteAll()

        val count = dao.countAll()
        assertThat(count).isEqualTo(0)
    }

    private fun createPluginEntity(
        id: String, isEnabled: Boolean = true
    ) = InstalledPluginEntity(
        id = id,
        name = "Anime Provider",
        version = 1,
        versionName = "1.0.0",
        description = "Watch anime",
        authors = "[\"John Doe\"]",
        iconUrl = "https://example.com/icon.png",
        language = "en",
        contentTypes = "[\"ANIME\"]",
        filePath = "/path/to/plugin.jar",
        repositoryUrl = "https://repo.example.com/manifest.json",
        requiresAppVersion = 1,
        isEnabled = isEnabled,
        isAdult = false,
        installedAt = 1234567890L,
        updatedAt = 1234567890L,
    )
}

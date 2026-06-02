package com.dreamstream.core.database.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.dreamstream.core.database.TestDatabaseFactory
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.plugin.InstalledPlugin
import com.dreamstream.core.model.plugin.PluginManifest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class PluginRepositoryTest {
    private val database = TestDatabaseFactory.createTestDatabase()
    private val repository = PluginRepository(database.pluginDao())

    @Test
    fun testInsertAndRead() = runTest {
        val plugin = createPlugin("1")

        repository.insert(plugin)

        val read = repository.getById("1")
        assertThat(read).isNotNull()
        assertThat(read?.manifest?.name).isEqualTo("Anime Provider")
    }

    @Test
    fun testObserveAll() = runTest {
        repository.insert(createPlugin("1"))
        repository.insert(createPlugin("2"))

        repository.observeAll().test {
            val list = awaitItem()
            assertThat(list).hasSize(2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testObserveEnabled() = runTest {
        repository.insert(createPlugin("1", isEnabled = true))
        repository.insert(createPlugin("2", isEnabled = false))

        repository.observeEnabled().test {
            val list = awaitItem()
            assertThat(list).hasSize(1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testSetEnabled() = runTest {
        val plugin = createPlugin("1", isEnabled = true)
        repository.insert(plugin)

        repository.setEnabled("1", false)

        val updated = repository.getById("1")
        assertThat(updated).isNotNull()
        assertThat(updated?.isEnabled).isEqualTo(false)
    }

    @Test
    fun testUpdateVersion() = runTest {
        val plugin = createPlugin("1")
        repository.insert(plugin)

        repository.updateVersion("1", 2, "2.0.0", "/new/path.jar", 1234567891L)

        val updated = repository.getById("1")
        assertThat(updated).isNotNull()
        assertThat(updated?.manifest?.version).isEqualTo(2)
        assertThat(updated?.manifest?.versionName).isEqualTo("2.0.0")
        assertThat(updated?.filePath).isEqualTo("/new/path.jar")
    }

    @Test
    fun testDeleteById() = runTest {
        val plugin = createPlugin("1")
        repository.insert(plugin)

        repository.deleteById("1")

        val deleted = repository.getById("1")
        assertThat(deleted).isNull()
    }

    private fun createPlugin(
        id: String, isEnabled: Boolean = true
    ) = InstalledPlugin(
        manifest = PluginManifest(
            id = id,
            name = "Anime Provider",
            version = 1,
            versionName = "1.0.0",
            description = "Watch anime",
            authors = listOf("John Doe"),
            iconUrl = "https://example.com/icon.png",
            language = "en",
            contentTypes = listOf(ContentType.Anime),
            url = "/path/to/plugin.jar",
            repositoryUrl = "https://repo.example.com/manifest.json",
            fileSize = 1_500_000L,
            sha256 = "abc123",
            requiresAppVersion = 1,
            changelog = null,
            repositoryName = null,
            isAdult = false
        ),
        filePath = "/path/to/plugin.jar",
        isEnabled = isEnabled,
        installedAt = 1234567890L,
        updatedAt = 1234567890L,
    )
}

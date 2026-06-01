package com.dreamstream.core.model.catalog

import com.dreamstream.core.model.user.WatchHistory
import kotlin.test.Test
import kotlin.test.assertEquals

class EpisodeTest {

    @Test
    fun `proper uniqueId should be generated for an episode`() {
        val episode = Episode(
            data = DATA,
            season = 2,
            episode = 5,
        )
        assertEquals("2_5_${DATA.hashCode()}", episode.uniqueId)
    }

    @Test
    fun `percentageWatched should clamp between 0 and 1`() {
        val entry = WatchHistory(
            id = "1",
            providerId = "test",
            url = DATA,
            title = "Test Movie",
            type = ContentType.Movie,
            watchPositionMs = 120_000,
            totalDurationMs = 100_000, // More than duration (edge case)
            lastWatchedAt = 0L,
            createdAt = 0L,
        )
        assertEquals(1.0f, entry.percentageWatched)
    }

    companion object {
        const val DATA = "https://example.com"
    }
}

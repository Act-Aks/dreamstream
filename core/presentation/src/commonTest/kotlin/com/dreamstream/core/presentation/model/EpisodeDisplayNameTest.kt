package com.dreamstream.core.presentation.model

import com.dreamstream.core.model.catalog.Episode
import com.dreamstream.core.presentation.ui.UiText
import kotlin.test.Test
import kotlin.test.assertEquals

class DisplayNameTest {

    @Test
    fun `DisplayName should format season and episode`() {
        val episode = Episode(
            data = "https://example.com",
            season = 2,
            episode = 5,
        )
        assertEquals(UiText.DynamicString("S02E05"), episode.displayName)
    }

    @Test
    fun `DisplayName should use name when available`() {
        val episode = Episode(
            data = "https://example.com",
            name = "The Pilot",
            season = 1,
            episode = 1,
        )
        assertEquals(UiText.DynamicString("The Pilot"), episode.displayName)
    }

}

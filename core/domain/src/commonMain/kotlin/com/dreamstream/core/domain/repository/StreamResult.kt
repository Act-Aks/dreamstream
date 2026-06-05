package com.dreamstream.core.domain.repository

import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import com.dreamstream.core.domain.util.DreamError

sealed interface StreamResult {
    data class Link(val link: StreamLink) : StreamResult
    data class SubtitleFound(val subtitle: Subtitle) : StreamResult
    data class ErrorResult(val error: DreamError) : StreamResult
    data object Complete : StreamResult
}

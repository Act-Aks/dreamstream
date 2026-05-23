package com.dreamstream.feature.details.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/** Type-safe Navigation3 route for the Details screen. */
@Serializable
data class DetailsRoute(val contentId: String) : NavKey

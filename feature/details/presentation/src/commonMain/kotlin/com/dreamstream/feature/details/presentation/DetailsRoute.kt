package com.dreamstream.feature.details.presentation

import com.dreamstream.core.presentation.navigation.AppRoute
import kotlinx.serialization.Serializable

/** Type-safe Navigation3 route for the Details screen. */
@Serializable
data class DetailsRoute(val contentId: String) : AppRoute

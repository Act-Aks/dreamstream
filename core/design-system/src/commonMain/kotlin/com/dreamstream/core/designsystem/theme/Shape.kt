package com.dreamstream.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * DreamStream shape ramp.
 *
 * Slightly more rounded than the Material defaults to fit the soft,
 * media-forward aesthetic. Surface containers (cards, sheets) lean on
 * [Shapes.medium] / [Shapes.large]; chips and buttons use [Shapes.small].
 */
internal val DreamStreamShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

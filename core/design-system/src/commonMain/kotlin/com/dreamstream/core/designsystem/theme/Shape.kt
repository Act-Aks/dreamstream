package com.dreamstream.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * DreamStream shape ramp.
 *
 * More aggressively rounded than Material defaults to complement the
 * glassmorphic aesthetic — softer radii give glass surfaces the fluid,
 * organic feel expected in a modern media app.
 *
 * Usage guidance:
 *   extraSmall  — chips, badges, tooltips
 *   small       — buttons, text fields, small cards
 *   medium      — content cards, list items
 *   large       — bottom sheets, panels, feature cards
 *   extraLarge  — hero cards, full-screen sheets, dialogs
 */
internal val DreamStreamShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

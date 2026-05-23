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
 * | Level       | Radius | Typical use                              |
 * |-------------|--------|------------------------------------------|
 * | extraSmall  |  8 dp  | Chips, badges, tooltips                  |
 * | small       | 12 dp  | Buttons, text fields, small cards        |
 * | medium      | 16 dp  | Content cards, list items                |
 * | large       | 24 dp  | Bottom sheets, panels, feature cards     |
 * | extraLarge  | 32 dp  | Hero cards, full-screen sheets, dialogs  |
 */
internal val DreamStreamShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

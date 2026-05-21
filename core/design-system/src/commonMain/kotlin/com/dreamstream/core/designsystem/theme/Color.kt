package com.dreamstream.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * DreamStream color tokens.
 *
 * Treat this file as the single source of truth for raw color values. Map
 * tokens into Material color schemes inside [DreamStreamColorScheme] rather
 * than referencing colors directly from composables.
 *
 * The palette below is intentionally a sober dark-first scaffolding. Tune it
 * once the brand direction is finalized — do not pepper bespoke colors
 * across feature modules.
 */
internal object DreamStreamPalette {
    // Brand
    val Primary = Color(0xFFB388FF)        // soft violet
    val OnPrimary = Color(0xFF1B0B3A)
    val PrimaryContainer = Color(0xFF3B1F77)
    val OnPrimaryContainer = Color(0xFFE9DDFF)

    val Secondary = Color(0xFF80DEEA)      // cyan accent
    val OnSecondary = Color(0xFF003138)
    val SecondaryContainer = Color(0xFF004F58)
    val OnSecondaryContainer = Color(0xFFB2EBF2)

    val Tertiary = Color(0xFFFFB74D)       // warm highlight
    val OnTertiary = Color(0xFF3E2700)
    val TertiaryContainer = Color(0xFF5C3A00)
    val OnTertiaryContainer = Color(0xFFFFDDB1)

    // Neutral surfaces
    val Background = Color(0xFF0E0F13)
    val OnBackground = Color(0xFFE6E1E9)
    val Surface = Color(0xFF131419)
    val OnSurface = Color(0xFFE6E1E9)
    val SurfaceVariant = Color(0xFF2B2A32)
    val OnSurfaceVariant = Color(0xFFC8C5D0)
    val Outline = Color(0xFF8E8C94)
    val OutlineVariant = Color(0xFF49474F)

    // Status
    val Error = Color(0xFFFFB4AB)
    val OnError = Color(0xFF690005)
    val ErrorContainer = Color(0xFF93000A)
    val OnErrorContainer = Color(0xFFFFDAD6)

    // Light-mode mirrors (used by [LightColorScheme]).
    val LightBackground = Color(0xFFFFFBFE)
    val LightOnBackground = Color(0xFF1C1B1F)
    val LightSurface = Color(0xFFFFFBFE)
    val LightOnSurface = Color(0xFF1C1B1F)
    val LightSurfaceVariant = Color(0xFFE7E0EC)
    val LightOnSurfaceVariant = Color(0xFF49454F)
}

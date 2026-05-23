package com.dreamstream.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * DreamStream color tokens — vibrant glassmorphic dark palette.
 *
 * Raw color values live here as the single source of truth. Map tokens into
 * Material color schemes through [DarkColorScheme] and [LightColorScheme] in
 * [Theme.kt]. Composables must never reference these tokens directly — always
 * consume `MaterialTheme.colorScheme.*` or [GlassDefaults].
 *
 * Design direction: deep-space dark with electric violet, cyan, and pink
 * accents. The palette is tuned for rich glassmorphic layering — ambient
 * color blobs in the background give glass surfaces a vibrant look even when
 * blur is unavailable on older devices.
 */
internal object DreamStreamPalette {

    // ── Brand Violet ──────────────────────────────────────────────────────────
    val Primary            = Color(0xFFA855F7)  // electric violet
    val OnPrimary          = Color(0xFF1E0040)
    val PrimaryContainer   = Color(0xFF4A0D80)
    val OnPrimaryContainer = Color(0xFFF0D9FF)

    // ── Electric Cyan ─────────────────────────────────────────────────────────
    val Secondary            = Color(0xFF06B6D4)  // electric cyan
    val OnSecondary          = Color(0xFF00202A)
    val SecondaryContainer   = Color(0xFF003544)
    val OnSecondaryContainer = Color(0xFFB2F5FF)

    // ── Vibrant Pink ──────────────────────────────────────────────────────────
    val Tertiary            = Color(0xFFF472B6)  // hot pink
    val OnTertiary          = Color(0xFF400028)
    val TertiaryContainer   = Color(0xFF7A004E)
    val OnTertiaryContainer = Color(0xFFFFD6EC)

    // ── Deep Space Surfaces ───────────────────────────────────────────────────
    // Near-black base with a very subtle cool-blue undertone so glass surfaces
    // read as dark but not flat gray.
    val Background         = Color(0xFF07080F)
    val OnBackground       = Color(0xFFECE8F6)
    val Surface            = Color(0xFF0C0D1A)
    val OnSurface          = Color(0xFFECE8F6)
    val SurfaceVariant     = Color(0xFF1A1B2E)  // dark indigo-tinted
    val OnSurfaceVariant   = Color(0xFFC9C4DD)
    val Outline            = Color(0xFF716B84)
    val OutlineVariant     = Color(0xFF3A3549)

    // ── Status ────────────────────────────────────────────────────────────────
    val Error            = Color(0xFFFF6B6B)
    val OnError          = Color(0xFF420000)
    val ErrorContainer   = Color(0xFF7A0000)
    val OnErrorContainer = Color(0xFFFFDAD6)

    // ── Glass Tints ───────────────────────────────────────────────────────────
    // Not mapped into the M3 color scheme — only read by [GlassDefaults].
    // White tints create the frosted-glass look; the primary tint adds the
    // faint violet hue characteristic of DreamStream's brand.
    val GlassWhiteTint   = Color(0x14FFFFFF)  //  8% white  — base frosted fill
    val GlassBorder      = Color(0x28FFFFFF)  // 16% white  — subtle rim light
    val GlassPrimaryTint = Color(0x14A855F7)  //  8% violet — brand tint on glass
    val GlassCyanTint    = Color(0x0E06B6D4)  //  6% cyan   — accent tint variant

    // ── Ambient Gradient Sources ──────────────────────────────────────────────
    // Painted by [GradientBackground] as soft colored glows behind all content.
    // Their transparency values are tuned so the sum of all blobs doesn't
    // overwhelm the dark base color.
    val AmbientPurple  = Color(0x55A855F7)  // 33% violet — upper-left glow
    val AmbientCyan    = Color(0x3006B6D4)  // 19% cyan   — upper-right glow
    val AmbientPink    = Color(0x22F472B6)  // 13% pink   — lower-right accent
    val AmbientBase    = Color(0xFF050611)  // opaque deep-space base

    // ── Light-Mode Mirrors ────────────────────────────────────────────────────
    // Light mode is a secondary target; tune these once the dark palette is
    // finalized and validated on device.
    val LightBackground       = Color(0xFFFAF7FF)
    val LightOnBackground     = Color(0xFF1C1A22)
    val LightSurface          = Color(0xFFFAF7FF)
    val LightOnSurface        = Color(0xFF1C1A22)
    val LightSurfaceVariant   = Color(0xFFEADFF6)
    val LightOnSurfaceVariant = Color(0xFF4A4459)
}

package com.dreamstream.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * DreamStream color tokens — vibrant glassmorphic dark palette.
 *
 * Raw color values live here as the single source of truth. Map tokens into
 * Material color schemes through [DarkColorScheme] and [LightColorScheme] in
 * [Theme.kt]. Composables must never reference these tokens directly — always
 * consume `MaterialTheme.colorScheme.*`, [GlassDefaults], or
 * `DreamStreamTheme.colors.*` for extended tokens.
 *
 * Design direction: deep-space dark with electric violet, cyan, and pink
 * accents. The palette is tuned for rich glassmorphic layering — ambient
 * color blobs in the background give glass surfaces a vibrant look even when
 * blur is unavailable on older devices.
 */
internal object DreamStreamPalette {

    // ── Brand Violet ──────────────────────────────────────────────────────────

    /** Electric violet — the DreamStream brand signature. */
    val Primary = Color(0xFFA855F7)

    /** Dark text and icon color rendered on [Primary]-coloured surfaces. */
    val OnPrimary = Color(0xFF1E0040)

    /** Container variant of [Primary] for lower-emphasis filled elements. */
    val PrimaryContainer = Color(0xFF4A0D80)

    /** Text and icon color inside [PrimaryContainer]. */
    val OnPrimaryContainer = Color(0xFFF0D9FF)

    // ── Electric Cyan ─────────────────────────────────────────────────────────

    /** Electric cyan — secondary brand accent for interactive and informational elements. */
    val Secondary = Color(0xFF06B6D4)

    /** Dark text and icon color rendered on [Secondary]-coloured surfaces. */
    val OnSecondary = Color(0xFF00202A)

    /** Container variant of [Secondary] for lower-emphasis filled elements. */
    val SecondaryContainer = Color(0xFF003544)

    /** Text and icon color inside [SecondaryContainer]. */
    val OnSecondaryContainer = Color(0xFFB2F5FF)

    // ── Vibrant Pink ──────────────────────────────────────────────────────────

    /** Vibrant hot pink — tertiary accent for highlights and hero moments. */
    val Tertiary = Color(0xFFF472B6)

    /** Dark text and icon color rendered on [Tertiary]-coloured surfaces. */
    val OnTertiary = Color(0xFF400028)

    /** Container variant of [Tertiary] for lower-emphasis filled elements. */
    val TertiaryContainer = Color(0xFF7A004E)

    /** Text and icon color inside [TertiaryContainer]. */
    val OnTertiaryContainer = Color(0xFFFFD6EC)

    // ── Deep Space Surfaces ───────────────────────────────────────────────────

    /**
     * Primary screen background — near-black with a subtle cool-blue undertone
     * so glass surfaces placed above read as dark but never flat gray.
     */
    val Background = Color(0xFF07080F)

    /** Text and icon color on [Background]. */
    val OnBackground = Color(0xFFECE8F6)

    /** Default surface color for cards and elevated containers. */
    val Surface = Color(0xFF0C0D1A)

    /** Text and icon color on [Surface]. */
    val OnSurface = Color(0xFFECE8F6)

    /** Dark indigo-tinted variant surface for chips, input backgrounds, and tooltips. */
    val SurfaceVariant = Color(0xFF1A1B2E)

    /** Text and icon color on [SurfaceVariant]. */
    val OnSurfaceVariant = Color(0xFFC9C4DD)

    /** Outline and divider color for component borders and separators. */
    val Outline = Color(0xFF716B84)

    /** Subtle outline variant for lower-emphasis borders. */
    val OutlineVariant = Color(0xFF3A3549)

    // ── Status ────────────────────────────────────────────────────────────────

    /** Warm coral red — error and destructive action indicator. */
    val Error = Color(0xFFFF6B6B)

    /** Text and icon color rendered on [Error]-coloured surfaces. */
    val OnError = Color(0xFF420000)

    /** Container variant of [Error] for error chips and banners. */
    val ErrorContainer = Color(0xFF7A0000)

    /** Text and icon color inside [ErrorContainer]. */
    val OnErrorContainer = Color(0xFFFFDAD6)

    // ── Glass Tints ───────────────────────────────────────────────────────────
    // Not mapped into the M3 color scheme. Use for non-Haze surfaces (nav bar,
    // chips, overlays) that need a translucent fill without full backdrop blur.

    /** 5% white — barely-there tint for tooltips and floating overlays. */
    val GlassThin = Color(0x0DFFFFFF)

    /** 8% white — base frosted fill; the default for most glass surfaces. */
    val GlassWhiteTint = Color(0x14FFFFFF)

    /** 12% white — standard card fill for elevated glass elements. */
    val GlassMedium = Color(0x1FFFFFFF)

    /** 17% white — fill for elevated surfaces such as panels and modal headers. */
    val GlassStrong = Color(0x2BFFFFFF)

    /** 16% white — subtle rim-light border for glass cards and containers. */
    val GlassBorder = Color(0x28FFFFFF)

    /** 30% white — border for focused or actively-selected glass elements. */
    val GlassBorderFocused = Color(0x4DFFFFFF)

    /** 8% [Primary] violet — brand tint layered on top of the white frosted fill. */
    val GlassPrimaryTint = Color(0x14A855F7)

    /** 6% [Secondary] cyan — alternate accent tint for glass surfaces. */
    val GlassCyanTint = Color(0x0E06B6D4)

    // ── Ambient Gradient Sources ──────────────────────────────────────────────
    // Painted by [GradientBackground] as soft radial glows behind all content.
    // Opacity values are calibrated so the sum of all three blobs does not
    // overwhelm the [AmbientBase] dark fill.

    /** 33% violet radial glow — placed upper-left by [GradientBackground]. */
    val AmbientPurple = Color(0x55A855F7)

    /** 19% cyan radial glow — placed upper-right by [GradientBackground]. */
    val AmbientCyan = Color(0x3006B6D4)

    /** 13% pink radial glow — placed lower-right by [GradientBackground]. */
    val AmbientPink = Color(0x22F472B6)

    /** Opaque deep-space base painted beneath all ambient glow blobs. */
    val AmbientBase = Color(0xFF050611)

    // ── Text Hierarchy ────────────────────────────────────────────────────────
    // All tokens carry a subtle violet undertone to stay on-brand. Prefer these
    // over M3's [OnSurface] / [OnSurfaceVariant] when a named semantic intent
    // such as disabled or placeholder is required.

    /** Soft white with a dream-violet tint — primary content text and titles. */
    val TextPrimary = Color(0xFFF0EEFF)

    /** Muted lavender-silver — secondary content text and metadata. */
    val TextSecondary = Color(0xFFB4B0D0)

    /** Dimmed dream purple — tertiary text for timestamps and minor labels. */
    val TextTertiary = Color(0xFF7A76A0)

    /** Ghost-level text — inactive controls and disabled states. */
    val TextDisabled = Color(0xFF3D3A5C)

    /** Placeholder text for empty input fields. */
    val TextPlaceholder = Color(0xFF4A4770)

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Bottom navigation bar background — 90%-opaque [Background] so the
     * ambient gradient bleeds through slightly.
     */
    val NavBarBg = Color(0xE607080F)

    /** Active (selected) tab icon and label color — reuses [Primary]. */
    val NavItemActive = Primary

    /** Inactive (unselected) tab icon and label color — reuses [TextTertiary]. */
    val NavItemInactive = TextTertiary

    // ── Semantic: Success / Warning / Info ────────────────────────────────────
    // Error is covered by M3's colorScheme.error. These three fill the gap.
    // The *Glass variants are 17%-alpha fills for toast and chip backgrounds.

    /** Emerald green — positive and success states. */
    val Success = Color(0xFF34D399)

    /** 17%-alpha emerald — fill for success chips and toast backgrounds. */
    val SuccessGlass = Color(0x2B34D399)

    /** Amber gold — caution and warning states. */
    val Warning = Color(0xFFFFCA28)

    /** 17%-alpha amber — fill for warning chips and banner backgrounds. */
    val WarningGlass = Color(0x2BFFCA28)

    /** Electric cyan — informational states; reuses [Secondary]. */
    val Info = Secondary

    /** 17%-alpha cyan — fill for info chips and tooltip backgrounds. */
    val InfoGlass = Color(0x2B06B6D4)

    // ── Light-Mode Mirrors ────────────────────────────────────────────────────
    // Light mode is a secondary target. Tune these values independently of
    // the dark palette once the dark appearance is finalized on device.

    /** Light-mode screen background — warm off-white with a violet tint. */
    val LightBackground = Color(0xFFFAF7FF)

    /** Text and icon color on [LightBackground]. */
    val LightOnBackground = Color(0xFF1C1A22)

    /** Light-mode surface color for cards and elevated containers. */
    val LightSurface = Color(0xFFFAF7FF)

    /** Text and icon color on [LightSurface]. */
    val LightOnSurface = Color(0xFF1C1A22)

    /** Light-mode variant surface for chips and input backgrounds. */
    val LightSurfaceVariant = Color(0xFFEADFF6)

    /** Text and icon color on [LightSurfaceVariant]. */
    val LightOnSurfaceVariant = Color(0xFF4A4459)
}

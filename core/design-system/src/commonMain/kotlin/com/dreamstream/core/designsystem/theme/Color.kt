package com.dreamstream.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.Background
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.Error
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.ErrorContainer
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.LightBackground
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.LightSurface
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.LightSurfaceVariant
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.Primary
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.PrimaryContainer
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.Secondary
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.SecondaryContainer
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.Surface
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.SurfaceVariant
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.Tertiary
import com.dreamstream.core.designsystem.theme.DreamStreamPalette.TertiaryContainer

/**
 * DreamStream color tokens — single source of truth for both dark and light themes.
 *
 * Raw color values live here only. Theme objects map these tokens into
 * Material color schemes and extended design-system colors.
 *
 * Composables must never reference these tokens directly — always consume
 * `MaterialTheme.colorScheme.*`, [GlassDefaults], or [DreamStreamTheme.colors.*]
 * for extended tokens.
 *
 * Design direction: deep-space dark with electric violet, cyan, and pink
 * accents. The palette is tuned for rich glassmorphic layering — ambient
 * color blobs in the background give glass surfaces a vibrant look even when
 * blur is unavailable on older devices.
 *
 * Groups:
 * - **Brand** — primary violet, secondary cyan, tertiary pink.
 * - **Surfaces** — dark and light backgrounds, surfaces, and variants.
 * - **Glass** — translucent tints and borders for glassmorphic UI.
 * - **Ambient** — radial glow sources for gradient backgrounds.
 * - **Text** — dark and light text hierarchy tokens.
 * - **Navigation** — bottom nav bar and tab colors.
 * - **Semantic** — success, warning, info, and error states.
 * - **Shimmer** — dark and light skeleton loading colors.
 */
internal object DreamStreamPalette {

    // ── Brand Violet ──────────────────────────────────────────────────────────

    /**
     * Electric violet — the DreamStream brand signature.
     *
     * Used for primary buttons, CTAs, and key interactive elements.
     */
    val Primary = Color(0xFFA855F7)

    /**
     * Dark text and icon color rendered on [Primary]-coloured surfaces.
     */
    val OnPrimary = Color(0xFF1E0040)

    /**
     * Container variant of [Primary] for lower-emphasis filled elements.
     *
     * Used for filled chips, badges, and secondary buttons.
     */
    val PrimaryContainer = Color(0xFF4A0D80)

    /**
     * Text and icon color inside [PrimaryContainer].
     */
    val OnPrimaryContainer = Color(0xFFF0D9FF)

    // ── Electric Cyan ─────────────────────────────────────────────────────────

    /**
     * Electric cyan — secondary brand accent for interactive and informational elements.
     *
     * Used for links, info badges, and secondary interactive states.
     */
    val Secondary = Color(0xFF06B6D4)

    /**
     * Dark text and icon color rendered on [Secondary]-coloured surfaces.
     */
    val OnSecondary = Color(0xFF00202A)

    /**
     * Container variant of [Secondary] for lower-emphasis filled elements.
     */
    val SecondaryContainer = Color(0xFF003544)

    /**
     * Text and icon color inside [SecondaryContainer].
     */
    val OnSecondaryContainer = Color(0xFFB2F5FF)

    // ── Vibrant Pink ──────────────────────────────────────────────────────────

    /**
     * Vibrant hot pink — tertiary accent for highlights and hero moments.
     *
     * Used for featured content labels, "New" badges, and hero accents.
     */
    val Tertiary = Color(0xFFF472B6)

    /**
     * Dark text and icon color rendered on [Tertiary]-coloured surfaces.
     */
    val OnTertiary = Color(0xFF400028)

    /**
     * Container variant of [Tertiary] for lower-emphasis filled elements.
     */
    val TertiaryContainer = Color(0xFF7A004E)

    /**
     * Text and icon color inside [TertiaryContainer].
     */
    val OnTertiaryContainer = Color(0xFFFFD6EC)

    // ── Deep Space Surfaces ───────────────────────────────────────────────────

    /**
     * Primary screen background — near-black with a subtle cool-blue undertone.
     *
     * So glass surfaces placed above read as dark but never flat gray.
     */
    val Background = Color(0xFF07080F)

    /**
     * Text and icon color on [Background].
     */
    val OnBackground = Color(0xFFECE8F6)

    /**
     * Default surface color for dark cards and elevated containers.
     */
    val Surface = Color(0xFF0C0D1A)

    /**
     * Text and icon color on [Surface].
     */
    val OnSurface = Color(0xFFECE8F6)

    /**
     * Dark indigo-tinted variant surface for chips, input backgrounds, and tooltips.
     */
    val SurfaceVariant = Color(0xFF1A1B2E)

    /**
     * Text and icon color on [SurfaceVariant].
     */
    val OnSurfaceVariant = Color(0xFFC9C4DD)

    /**
     * Outline and divider color for component borders and separators.
     */
    val Outline = Color(0xFF716B84)

    /**
     * Subtle outline variant for lower-emphasis borders.
     */
    val OutlineVariant = Color(0xFF3A3549)

    // ── Status ────────────────────────────────────────────────────────────────

    /**
     * Warm coral red — error and destructive action indicator.
     */
    val Error = Color(0xFFFF6B6B)

    /**
     * Text and icon color rendered on [Error]-coloured surfaces.
     */
    val OnError = Color(0xFF420000)

    /**
     * Container variant of [Error] for error chips and banners.
     */
    val ErrorContainer = Color(0xFF7A0000)

    /**
     * Text and icon color inside [ErrorContainer].
     */
    val OnErrorContainer = Color(0xFFFFDAD6)

    // ── Glass Tints ───────────────────────────────────────────────────────────

    /**
     * 5% white — barely-there tint for tooltips and floating overlays.
     */
    val GlassThin = Color(0x0DFFFFFF)

    /**
     * 8% white — base frosted fill; the default for most glass surfaces.
     */
    val GlassWhiteTint = Color(0x14FFFFFF)

    /**
     * 12% white — standard card fill for elevated glass elements.
     */
    val GlassMedium = Color(0x1FFFFFFF)

    /**
     * 17% white — fill for elevated surfaces such as panels and modal headers.
     */
    val GlassStrong = Color(0x2BFFFFFF)

    /**
     * 16% white — subtle rim-light border for glass cards and containers.
     */
    val GlassBorder = Color(0x28FFFFFF)

    /**
     * 30% white — border for focused or actively-selected glass elements.
     */
    val GlassBorderFocused = Color(0x4DFFFFFF)

    /**
     * 8% [Primary] violet — brand tint layered on top of the white frosted fill.
     */
    val GlassPrimaryTint = Color(0x14A855F7)

    /**
     * 6% [Secondary] cyan — alternate accent tint for glass surfaces.
     */
    val GlassCyanTint = Color(0x0E06B6D4)

    // ── Ambient Gradient Sources ──────────────────────────────────────────────

    /**
     * Violet radial glow for the background ambient layer.
     */
    val AmbientPurple = Color(0x55A855F7)

    /**
     * Cyan radial glow for the background ambient layer.
     */
    val AmbientCyan = Color(0x3006B6D4)

    /**
     * Pink radial glow for the background ambient layer.
     */
    val AmbientPink = Color(0x22F472B6)

    /**
     * Opaque deep-space base behind all ambient glows.
     */
    val AmbientBase = Color(0xFF050611)

    // ── Text Hierarchy ────────────────────────────────────────────────────────

    /**
     * Primary content text and titles in dark theme.
     *
     * Soft white with a dream-violet tint.
     */
    val TextPrimary = Color(0xFFF0EEFF)

    /**
     * Secondary content text and metadata in dark theme.
     *
     * Muted lavender-silver.
     */
    val TextSecondary = Color(0xFFB4B0D0)

    /**
     * Tertiary text for timestamps and minor labels in dark theme.
     *
     * Dimmed dream purple.
     */
    val TextTertiary = Color(0xFF7A76A0)

    /**
     * Disabled / inactive text in dark theme.
     *
     * Ghost-level emphasis.
     */
    val TextDisabled = Color(0xFF3D3A5C)

    /**
     * Placeholder text for empty input fields in dark theme.
     */
    val TextPlaceholder = Color(0xFF4A4770)

    /**
     * Primary content text and titles in light theme.
     */
    val LightTextPrimary = Color(0xFF1C1A22)

    /**
     * Secondary content text and metadata in light theme.
     */
    val LightTextSecondary = Color(0xFF4A4459)

    /**
     * Tertiary text for timestamps and minor labels in light theme.
     */
    val LightTextTertiary = Color(0xFF6A657C)

    /**
     * Disabled / inactive text in light theme.
     */
    val LightTextDisabled = Color(0xFF9A95B0)

    /**
     * Placeholder text for empty input fields in light theme.
     */
    val LightTextPlaceholder = Color(0xFF8E88A8)

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Bottom navigation bar background in dark theme.
     *
     * 90%-opaque [Background] so the ambient gradient bleeds through slightly.
     */
    val NavBarBg = Color(0xE607080F)

    /**
     * Bottom navigation bar background in light theme.
     */
    val LightNavBarBg = Color(0xE6FAF7FF)

    /**
     * Active (selected) tab icon and label color.
     *
     * Reuses [Primary].
     */
    val NavItemActive = Primary

    /**
     * Inactive (unselected) tab icon and label color in dark theme.
     */
    val NavItemInactive = TextTertiary

    /**
     * Inactive (unselected) tab icon and label color in light theme.
     */
    val LightNavItemInactive = LightTextTertiary

    // ── Semantic: Success / Warning / Info ────────────────────────────────────

    /**
     * Emerald green — positive and success states.
     */
    val Success = Color(0xFF34D399)

    /**
     * 17%-alpha emerald — fill for success chips and toast backgrounds.
     */
    val SuccessGlass = Color(0x2B34D399)

    /**
     * Amber gold — caution and warning states.
     */
    val Warning = Color(0xFFFFCA28)

    /**
     * 17%-alpha amber — fill for warning chips and banner backgrounds.
     */
    val WarningGlass = Color(0x2BFFCA28)

    /**
     * Electric cyan — informational states; reuses [Secondary].
     */
    val Info = Secondary

    /**
     * 17%-alpha cyan — fill for info chips and tooltip backgrounds.
     */
    val InfoGlass = Color(0x2B06B6D4)

    // ── Shimmer ───────────────────────────────────────────────────────────────

    /**
     * Dark shimmer base for skeleton placeholders in dark theme.
     */
    val DarkShimmerBase = Color(0xFF1A1B2E)

    /**
     * Bright shimmer band for skeleton placeholders in dark theme.
     */
    val DarkShimmerHighlight = Color(0xFF2D2E48)

    /**
     * Light shimmer base for skeleton placeholders in light theme.
     */
    val LightShimmerBase = Color(0xFFE2E2F0)

    /**
     * Bright shimmer band for skeleton placeholders in light theme.
     */
    val LightShimmerHighlight = Color(0xFFF0F0FF)

    // ── Light-Mode Mirrors ────────────────────────────────────────────────────

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

    // ── Brand Gold ────────────────────────────────────────────────────────────

    /**
     * Premium gold accent — used for highlights, ratings, and VIP / premium UI.
     *
     * Tuned to look good on dark backgrounds and as a shimmer highlight.
     */
    val Gold = Color(0xFFEFBF04)
}

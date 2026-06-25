package com.dreamstream.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// Extended color system
// ─────────────────────────────────────────────────────────────────────────────

/**
 * DreamStream-specific colors that have no slot in the Material3 color scheme.
 *
 * Access via [DreamStreamTheme.colors] inside any composable that is wrapped
 * in [DreamStreamTheme]. Never instantiate this class directly in production
 * code — use the singleton wired up by the theme.
 *
 * Groups:
 * - **Glass tints** — translucent fills for non-Haze surfaces (nav bar, chips,
 *   overlays) where backdrop blur is not required.
 * - **Text hierarchy** — semantic names for the five text emphasis levels;
 *   prefer these over raw [Color.White] or [ColorScheme.onSurface].
 * - **Navigation** — bottom nav bar background and tab indicator colors.
 * - **Semantic** — success, warning, and info states. Error is already covered
 *   by [ColorScheme.error].
 * - **Shimmer** — skeleton loading colors for placeholder surfaces.
 */
@Immutable
data class DreamStreamExtendedColors(
    // ── Glass tints ───────────────────────────────────────────────────────────
    /** 5% white — barely-there tint for tooltips and subtle overlays. */
    val glassThin: Color,
    /** 8% white — base frosted fill (default for most glass surfaces). */
    val glassLight: Color,
    /** 12% white — standard card fill. */
    val glassMedium: Color,
    /** 17% white — elevated surfaces (panels, modal headers). */
    val glassStrong: Color,
    /** 16% white — default rim-light border. */
    val glassBorder: Color,
    /** 30% white — border for focused / active elements. */
    val glassBorderFocused: Color,

    // ── Text hierarchy ────────────────────────────────────────────────────────
    /** Primary content text — soft white with a dream-violet tint. */
    val textPrimary: Color,
    /** Secondary content text — muted lavender-silver for metadata. */
    val textSecondary: Color,
    /** Tertiary content text — dimmed dream purple for timestamps, labels. */
    val textTertiary: Color,
    /** Disabled / inactive text — ghost-level emphasis. */
    val textDisabled: Color,
    /** Input field placeholder text. */
    val textPlaceholder: Color,

    // ── Navigation ────────────────────────────────────────────────────────────
    /** Bottom navigation bar background — 90% opaque deep-space fill. */
    val navBarBackground: Color,
    /** Active (selected) tab icon / label color. */
    val navItemActive: Color,
    /** Inactive (unselected) tab icon / label color. */
    val navItemInactive: Color,

    // ── Semantic ──────────────────────────────────────────────────────────────
    /** Success state — emerald green. */
    val success: Color,
    /** 17%-alpha emerald fill for success chips / toasts. */
    val successGlass: Color,
    /** Warning state — amber gold. */
    val warning: Color,
    /** 17%-alpha amber fill for warning chips / banners. */
    val warningGlass: Color,
    /** Info state — electric cyan (matches brand Secondary). */
    val info: Color,
    /** 17%-alpha cyan fill for info chips / tooltips. */
    val infoGlass: Color,

    // ── Shimmer ───────────────────────────────────────────────────────────────
    /** Dark shimmer base for skeleton placeholders in dark theme. */
    val shimmerBase: Color,
    /** Bright shimmer band for dark theme skeleton placeholders. */
    val shimmerHighlight: Color,

    // ── Ambient background ──────────────────────────────────────────────────
    /** Deep-space background base used by GradientBackground. */
    val ambientBase: Color,
    val ambientPurple: Color,
    val ambientCyan: Color,
    val ambientPink: Color,

    // ── Brand gold ────────────────────────────────────────────────────────────
    /** Premium gold accent for ratings, VIP, and glossy highlights. */
    val gold: Color,

    // ── Quality badges ───────────────────────────────────────────────────────
    /** Gray-ish for low/cam quality labels. */
    val qualityCam: Color,
    /** SD quality color. */
    val qualitySd: Color,
    /** HD quality color. */
    val qualityHd: Color,
    /** Full HD quality color. */
    val qualityFhd: Color,
    /** 4K quality color. */
    val qualityFourK: Color,
    /** Blu-ray quality color. */
    val qualityBluRay: Color,
)

// ─────────────────────────────────────────────────────────────────────────────
// CompositionLocal
// ─────────────────────────────────────────────────────────────────────────────

/**
 * CompositionLocal that carries [DreamStreamExtendedColors] down the tree.
 * Provided by [DreamStreamTheme]; throws a clear error if accessed outside it.
 */
val LocalDreamStreamColors = staticCompositionLocalOf<DreamStreamExtendedColors> {
    error(
        "DreamStreamExtendedColors not provided. " +
            "Wrap your content in DreamStreamTheme { }.",
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Material3 color schemes
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Dark scheme — DreamStream's default appearance. The product is media-first
 * and intended to be calm against video content.
 */
private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = DreamStreamPalette.Primary,
    onPrimary = DreamStreamPalette.OnPrimary,
    primaryContainer = DreamStreamPalette.PrimaryContainer,
    onPrimaryContainer = DreamStreamPalette.OnPrimaryContainer,
    secondary = DreamStreamPalette.Secondary,
    onSecondary = DreamStreamPalette.OnSecondary,
    secondaryContainer = DreamStreamPalette.SecondaryContainer,
    onSecondaryContainer = DreamStreamPalette.OnSecondaryContainer,
    tertiary = DreamStreamPalette.Tertiary,
    onTertiary = DreamStreamPalette.OnTertiary,
    tertiaryContainer = DreamStreamPalette.TertiaryContainer,
    onTertiaryContainer = DreamStreamPalette.OnTertiaryContainer,
    background = DreamStreamPalette.Background,
    onBackground = DreamStreamPalette.OnBackground,
    surface = DreamStreamPalette.Surface,
    onSurface = DreamStreamPalette.OnSurface,
    surfaceVariant = DreamStreamPalette.SurfaceVariant,
    onSurfaceVariant = DreamStreamPalette.OnSurfaceVariant,
    outline = DreamStreamPalette.Outline,
    outlineVariant = DreamStreamPalette.OutlineVariant,
    error = DreamStreamPalette.Error,
    onError = DreamStreamPalette.OnError,
    errorContainer = DreamStreamPalette.ErrorContainer,
    onErrorContainer = DreamStreamPalette.OnErrorContainer,
)

/**
 * Light scheme — offered for completeness; the app's chrome should still
 * default to dark. Tune separately from the dark palette rather than mirroring.
 */
private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = DreamStreamPalette.Primary,
    onPrimary = DreamStreamPalette.OnPrimary,
    primaryContainer = DreamStreamPalette.PrimaryContainer,
    onPrimaryContainer = DreamStreamPalette.OnPrimaryContainer,
    secondary = DreamStreamPalette.Secondary,
    onSecondary = DreamStreamPalette.OnSecondary,
    secondaryContainer = DreamStreamPalette.SecondaryContainer,
    onSecondaryContainer = DreamStreamPalette.OnSecondaryContainer,
    tertiary = DreamStreamPalette.Tertiary,
    onTertiary = DreamStreamPalette.OnTertiary,
    tertiaryContainer = DreamStreamPalette.TertiaryContainer,
    onTertiaryContainer = DreamStreamPalette.OnTertiaryContainer,
    background = DreamStreamPalette.LightBackground,
    onBackground = DreamStreamPalette.LightOnBackground,
    surface = DreamStreamPalette.LightSurface,
    onSurface = DreamStreamPalette.LightOnSurface,
    surfaceVariant = DreamStreamPalette.LightSurfaceVariant,
    onSurfaceVariant = DreamStreamPalette.LightOnSurfaceVariant,
    outline = DreamStreamPalette.Outline,
    outlineVariant = DreamStreamPalette.OutlineVariant,
    error = DreamStreamPalette.Error,
    onError = DreamStreamPalette.OnError,
    errorContainer = DreamStreamPalette.ErrorContainer,
    onErrorContainer = DreamStreamPalette.OnErrorContainer,
)

// ─────────────────────────────────────────────────────────────────────────────
// Extended colors instance
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Single shared instance of [DreamStreamExtendedColors] provided by the theme.
 * All values are drawn from [DreamStreamPalette] — no magic numbers here.
 */
private val DarkExtendedColors = DreamStreamExtendedColors(
    glassThin          = DreamStreamPalette.GlassThin,
    glassLight         = DreamStreamPalette.GlassWhiteTint,
    glassMedium        = DreamStreamPalette.GlassMedium,
    glassStrong        = DreamStreamPalette.GlassStrong,
    glassBorder        = DreamStreamPalette.GlassBorder,
    glassBorderFocused = DreamStreamPalette.GlassBorderFocused,
    textPrimary        = DreamStreamPalette.TextPrimary,
    textSecondary      = DreamStreamPalette.TextSecondary,
    textTertiary       = DreamStreamPalette.TextTertiary,
    textDisabled       = DreamStreamPalette.TextDisabled,
    textPlaceholder    = DreamStreamPalette.TextPlaceholder,
    navBarBackground   = DreamStreamPalette.NavBarBg,
    navItemActive      = DreamStreamPalette.NavItemActive,
    navItemInactive    = DreamStreamPalette.NavItemInactive,
    success            = DreamStreamPalette.Success,
    successGlass       = DreamStreamPalette.SuccessGlass,
    warning            = DreamStreamPalette.Warning,
    warningGlass       = DreamStreamPalette.WarningGlass,
    info               = DreamStreamPalette.Info,
    infoGlass          = DreamStreamPalette.InfoGlass,
    shimmerBase        = DreamStreamPalette.DarkShimmerBase,
    shimmerHighlight   = DreamStreamPalette.DarkShimmerHighlight,
    ambientBase        = DreamStreamPalette.AmbientBase,
    ambientPurple      = DreamStreamPalette.AmbientPurple,
    ambientCyan        = DreamStreamPalette.AmbientCyan,
    ambientPink        = DreamStreamPalette.AmbientPink,
    gold               = DreamStreamPalette.Gold,
    qualityCam         = DreamStreamPalette.TextTertiary,
    qualitySd          = DreamStreamPalette.Info,
    qualityHd          = DreamStreamPalette.Success,
    qualityFhd         = DreamStreamPalette.Primary,
    qualityFourK       = DreamStreamPalette.Warning,
    qualityBluRay      = DreamStreamPalette.Tertiary,
)

/**
 * Light-mode extended colors. These are tuned independently of the dark
 * palette and should feel softer, brighter, and less saturated.
 */
private val LightExtendedColors = DreamStreamExtendedColors(
    glassThin           = DreamStreamPalette.GlassThin,
    glassLight          = DreamStreamPalette.GlassWhiteTint,
    glassMedium         = DreamStreamPalette.GlassMedium,
    glassStrong         = DreamStreamPalette.GlassStrong,
    glassBorder         = DreamStreamPalette.GlassBorder,
    glassBorderFocused  = DreamStreamPalette.GlassBorderFocused,
    textPrimary         = DreamStreamPalette.LightTextPrimary,
    textSecondary       = DreamStreamPalette.LightTextSecondary,
    textTertiary        = DreamStreamPalette.LightTextTertiary,
    textDisabled        = DreamStreamPalette.LightTextDisabled,
    textPlaceholder     = DreamStreamPalette.LightTextPlaceholder,
    navBarBackground    = DreamStreamPalette.LightNavBarBg,
    navItemActive       = DreamStreamPalette.Primary,
    navItemInactive     = DreamStreamPalette.LightNavItemInactive,
    success             = DreamStreamPalette.Success,
    successGlass        = DreamStreamPalette.SuccessGlass,
    warning             = DreamStreamPalette.Warning,
    warningGlass        = DreamStreamPalette.WarningGlass,
    info                = DreamStreamPalette.Info,
    infoGlass           = DreamStreamPalette.InfoGlass,
    shimmerBase         = DreamStreamPalette.LightShimmerBase,
    shimmerHighlight    = DreamStreamPalette.LightShimmerHighlight,
    ambientBase         = DreamStreamPalette.AmbientBase,
    ambientPurple       = DreamStreamPalette.AmbientPurple,
    ambientCyan         = DreamStreamPalette.AmbientCyan,
    ambientPink         = DreamStreamPalette.AmbientPink,
    gold                = DreamStreamPalette.Gold,
    qualityCam          = DreamStreamPalette.TextTertiary,
    qualitySd           = DreamStreamPalette.Info,
    qualityHd           = DreamStreamPalette.Success,
    qualityFhd          = DreamStreamPalette.Primary,
    qualityFourK        = DreamStreamPalette.Warning,
    qualityBluRay       = DreamStreamPalette.Tertiary,
)

// ─────────────────────────────────────────────────────────────────────────────
// Theme composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Root theme entry point.
 *
 * Wrap every Compose hierarchy in [DreamStreamTheme] — including previews — so
 * tokens, typography, and shapes stay consistent. Avoid wrapping in
 * `MaterialTheme` directly; always go through this composable.
 *
 * Provides both [MaterialTheme] (colors, typography, shapes) and
 * [LocalDreamStreamColors] (extended tokens not covered by M3).
 * Access extended colors via [DreamStreamTheme.colors].
 *
 * @param darkTheme whether to render the dark color scheme (default: true,
 *   the product's intended look).
 */
@Composable
fun DreamStreamTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalDreamStreamColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = DreamStreamTypography,
            shapes = DreamStreamShapes,
            content = content,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Theme accessor object
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Convenient accessors for DreamStream theme values inside composables.
 *
 * Mirrors the pattern used by [MaterialTheme] in the Compose source — a
 * top-level function and a top-level object can share the same name in Kotlin.
 *
 * ```kotlin
 * // Extended (non-M3) tokens:
 * val colors = DreamStreamTheme.colors
 * Text(color = colors.textSecondary, ...)
 *
 * // Standard M3 tokens (same as MaterialTheme.colorScheme):
 * val scheme = DreamStreamTheme.materialColors
 * Text(color = scheme.primary, ...)
 * ```
 */
object DreamStreamTheme {
    /** DreamStream extended color tokens (text, nav, glass tints, semantic, shimmer). */
    val colors: DreamStreamExtendedColors
        @Composable @ReadOnlyComposable get() = LocalDreamStreamColors.current

    /** Local spacing. */
    val spacing: Spacing
        @Composable @ReadOnlyComposable get() = LocalSpacing.current

    /** Material3 color scheme — equivalent to [MaterialTheme.colorScheme]. */
    val materialColors: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    /** Material3 shapes — equivalent to [MaterialTheme.shapes]. */
    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes

    /** Material3 typography — equivalent to [MaterialTheme.typography]. */
    val typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography
}

package com.dreamstream.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * DreamStream raw color tokens.
 *
 * This file contains low-level, un-opinionated color values. These tokens do
 * NOT encode any Material roles or design-system semantics by themselves.
 *
 * This keeps the palette centralized while allowing flexible theming on top.
 */

// ── Brand: Violet ─────────────────────────────────────────────────────────────

internal val PurplePrimary = Color(0xFFA855F7)
internal val PurpleOnPrimary = Color(0xFF1E0040)
internal val PurplePrimaryContainer = Color(0xFF4A0D80)
internal val PurpleOnPrimaryContainer = Color(0xFFF0D9FF)

// ── Brand: Electric Cyan ──────────────────────────────────────────────────────

internal val CyanSecondary = Color(0xFF06B6D4)
internal val CyanOnSecondary = Color(0xFF00202A)
internal val CyanSecondaryContainer = Color(0xFF003544)
internal val CyanOnSecondaryContainer = Color(0xFFB2F5FF)

// ── Brand: Vibrant Pink ───────────────────────────────────────────────────────

internal val PinkTertiary = Color(0xFFF472B6)
internal val PinkOnTertiary = Color(0xFF400028)
internal val PinkTertiaryContainer = Color(0xFF7A004E)
internal val PinkOnTertiaryContainer = Color(0xFFFFD6EC)

// ── Deep Space Surfaces: Dark ─────────────────────────────────────────────────

internal val BackgroundDark = Color(0xFF07080F)
internal val OnBackgroundDark = Color(0xFFECE8F6)
internal val SurfaceDark = Color(0xFF0C0D1A)
internal val OnSurfaceDark = Color(0xFFECE8F6)
internal val SurfaceVariantDark = Color(0xFF1A1B2E)
internal val OnSurfaceVariantDark = Color(0xFFC9C4DD)
internal val OutlineDark = Color(0xFF716B84)
internal val OutlineVariantDark = Color(0xFF3A3549)

// ── Status / Error ────────────────────────────────────────────────────────────

internal val Error = Color(0xFFFF6B6B)
internal val OnError = Color(0xFF420000)
internal val ErrorContainer = Color(0xFF7A0000)
internal val OnErrorContainer = Color(0xFFFFDAD6)

// ── Glass Tints ───────────────────────────────────────────────────────────────

internal val GlassThin = Color(0x0DFFFFFF)
internal val GlassWhiteTint = Color(0x14FFFFFF)
internal val GlassMedium = Color(0x1FFFFFFF)
internal val GlassStrong = Color(0x2BFFFFFF)
internal val GlassBorder = Color(0x28FFFFFF)
internal val GlassBorderFocused = Color(0x4DFFFFFF)
internal val GlassPrimaryTint = Color(0x14A855F7)
internal val GlassCyanTint = Color(0x0E06B6D4)

// ── Ambient Gradient Sources ──────────────────────────────────────────────────

internal val AmbientPurple = Color(0x55A855F7)
internal val AmbientCyan = Color(0x3006B6D4)
internal val AmbientPink = Color(0x22F472B6)
internal val AmbientBase = Color(0xFF050611)

// ── Text Hierarchy: Dark ──────────────────────────────────────────────────────

internal val TextPrimaryDark = Color(0xFFF0EEFF)
internal val TextSecondaryDark = Color(0xFFB4B0D0)
internal val TextTertiaryDark = Color(0xFF7A76A0)
internal val TextDisabledDark = Color(0xFF3D3A5C)
internal val TextPlaceholderDark = Color(0xFF4A4770)

// ── Text Hierarchy: Light ─────────────────────────────────────────────────────

internal val TextPrimaryLight = Color(0xFF1C1A22)
internal val TextSecondaryLight = Color(0xFF4A4459)
internal val TextTertiaryLight = Color(0xFF6A657C)
internal val TextDisabledLight = Color(0xFF9A95B0)
internal val TextPlaceholderLight = Color(0xFF8E88A8)

// ── Semantic: Success / Warning / Info ───────────────────────────────────────

internal val Success = Color(0xFF34D399)
internal val Warning = Color(0xFFFFCA28)
internal val Info = CyanSecondary

// ── Shimmer Skeletons ─────────────────────────────────────────────────────────

internal val ShimmerBaseDark = Color(0xFF1A1B2E)
internal val ShimmerHighlightDark = Color(0xFF2D2E48)
internal val ShimmerBaseLight = Color(0xFFE2E2F0)
internal val ShimmerHighlightLight = Color(0xFFF0F0FF)

// ── Light-Mode Surfaces ──────────────────────────────────────────────────────

internal val BackgroundLight = Color(0xFFFAF7FF)
internal val OnBackgroundLight = Color(0xFF1C1A22)
internal val SurfaceLight = Color(0xFFFAF7FF)
internal val OnSurfaceLight = Color(0xFF1C1A22)
internal val SurfaceVariantLight = Color(0xFFEADFF6)
internal val OnSurfaceVariantLight = Color(0xFF4A4459)
internal val OutlineLight = Color(0xFF716B84)
internal val OutlineVariantLight = Color(0xFF3A3549)

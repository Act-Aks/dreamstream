package com.dreamstream.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * DreamStream spacing tokens — centralized system for padding, gaps, and margins.
 *
 * This spacing system follows a 4dp base scale, ensuring consistent rhythm across
 * layouts. All spacing values are multiples of 4dp for alignment with the design grid.
 *
 * Access spacing via [DreamStreamTheme.spacing] inside composables wrapped in
 * [DreamStreamTheme]. Never use hardcoded dp values in production code.
 *
 * Usage:
 * ```
 * val spacing = DreamStreamTheme.spacing
 * Column(
 *     modifier = Modifier.padding(spacing.md),
 *     verticalArrangement = Arrangement.spacedBy(spacing.gapSm)
 * ) {
 *     // Content
 * }
 * ```
 *
 * Naming Convention:
 * - **Scale** — `px`, `xs`, `sm`, `md`, `lg`, `xl`, `xxl`, `xxxl` for general gaps.
 * - **Contextual** — `padScreen`, `padCard`, `padInline` for common patterns.
 * - **Gaps** — `gapSm`, `gapMd`, `gapLg` for list/grid item spacing.
 *
 * Scale Reference:
 * - `px` = 4dp (extra tiny)
 * - `xs` = 8dp (tiny)
 * - `sm` = 12dp (small)
 * - `md` = 16dp (medium)
 * - `lg` = 24dp (large)
 * - `xl` = 32dp (extra large)
 * - `2xl` = 48dp (2x large)
 * - `3xl` = 64dp (3x large)
 */
@Immutable
data class Spacing(
    /**
     * Zero spacing — explicitly remove padding or gaps.
     */
    val zero: Dp = 0.dp,

    /**
     * Pixel spacing — minimal 4dp gap for ultra-tight layouts.
     *
     * Use for:
     * - Micro spacing in compact chips
     * - Tight icon + label gaps
     * - Minimal borders
     */
    val px: Dp = 4.dp,

    /**
     * Extra-small spacing — 8dp default for compact gaps.
     *
     * Use for:
     * - Icon + text in navigation items
     * - Padding in small buttons
     * - Tight list item gaps
     */
    val xs: Dp = 8.dp,

    /**
     * Small spacing — 12dp for compact content padding.
     *
     * Use for:
     * - Padding in medium buttons
     * - Filter chip padding
     * - Compact list gaps
     */
    val sm: Dp = 12.dp,

    /**
     * Medium spacing — 16dp default for content.
     *
     * The most common spacing value. Use for:
     * - Card inner padding
     * - Row/column padding
     * - Standard element gaps
     */
    val md: Dp = 16.dp,

    /**
     * Large spacing — 24dp for prominent gaps.
     *
     * Use for:
     * - Section padding
     * - Form layout spacing
     * - Unrelated element gaps
     */
    val lg: Dp = 24.dp,

    /**
     * Extra-large spacing — 32dp for major separation.
     *
     * Use for:
     * - Screen section gaps
     * - Full-width container padding
     * - Hero element spacing
     */
    val xl: Dp = 32.dp,

    /**
     * Double-extra spacing — 48dp for dramatic separation.
     *
     * Use for:
     * - Major page sections
     * - Modal overlay padding
     * - Onboarding screens
     */
    val xxl: Dp = 48.dp,

    /**
     * Triple-extra spacing — 64dp for maximum separation.
     *
     * Reserved for:
     * - Full-screen dividers
     * - Splash hero gaps
     * - Dramatic negative space
     */
    val xxxl: Dp = 64.dp,

    // ── Contextual Padding ────────────────────────────────────────────────────

    /**
     * Screen padding — default horizontal padding for screen content.
     *
     * Applies to screen edges and safe area insets:
     * ```
     * Column(modifier = Modifier.padding(horizontal = spacing.padScreen))
     * ```
     */
    val padScreen: Dp = 16.dp,

    /**
     * Card padding — default inner padding for content cards.
     *
     * Use inside media cards, content containers, and elevated surfaces.
     */
    val padCard: Dp = 16.dp,

    /**
     * Inline padding — compact padding for inline elements.
     *
     * Use for:
     * - Inline chips and tags
     * - Compact buttons
     * - Inline controls
     */
    val padInline: Dp = 12.dp,

    /**
     * List padding — padding for list item content.
     *
     * Use inside list items to separate content from edges.
     */
    val padList: Dp = 12.dp,

    // ── Gaps (Item Spacing) ───────────────────────────────────────────────────

    /**
     * Small gap — 8dp between list/grid items.
     *
     * Use for:
     * ```
     * Arrangement.spacedBy(spacing.gapSm)
     * ```
     */
    val gapSm: Dp = 8.dp,

    /**
     * Medium gap — 16dp between typical elements.
     *
     * Use for standard element gaps in rows/columns.
     */
    val gapMd: Dp = 16.dp,

    /**
     * Large gap — 24dp between section content.
     *
     * Use for section-level spacing in layouts.
     */
    val gapLg: Dp = 24.dp,

    /**
     * Section gap — 32dp between major sections.
     *
     * Use for screen-level section separation.
     */
    val gapSection: Dp = 32.dp,
)

/**
 * CompositionLocal carrying [Spacing] down the tree.
 *
 * Provided by [DreamStreamTheme]. Access via [DreamStreamTheme.spacing].
 */
val LocalSpacing = compositionLocalOf { Spacing() }

/**
 * Extension property for accessing [Spacing] from [Composer].
 *
 * Prefer [DreamStreamTheme.spacing] in composables.
 */
val Composer.spacing: Spacing
    @Composable @ReadOnlyComposable get() = LocalSpacing.current

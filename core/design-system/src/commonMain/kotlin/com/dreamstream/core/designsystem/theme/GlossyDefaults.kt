package com.dreamstream.core.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.GlassDefaults

/**
 * Default glossy styling tokens used by glassmorphic UI components.
 *
 * These values are intentionally centralized so cards, bars, chips, and
 * navigation items all share the same visual language.
 *
 * Import and use these by name from glossy components rather than constructing
 * values inline.
 */
object GlossyDefaults {

    /**
     * Default corner radius for glossy cards and surfaces.
     */
    val CornerRadius: Dp = 24.dp

    /**
     * Smaller corner radius for compact glossy elements.
     */
    val CompactCornerRadius: Dp = 16.dp

    /**
     * Radius used for pill-like chips.
     */
    val PillCornerRadius: Dp = 999.dp

    /**
     * Standard horizontal content padding inside glass surfaces.
     */
    val HorizontalPadding: Dp = 16.dp

    /**
     * Standard vertical content padding inside glass surfaces.
     */
    val VerticalPadding: Dp = 16.dp

    /**
     * Thin border width for glossy surfaces.
     */
    val BorderWidth: Dp = 1.dp

    /**
     * Slightly stronger border width for elevated glossy containers.
     */
    val StrongBorderWidth: Dp = 1.5.dp

    /**
     * Ultra-thin border width for subtle glossy elements.
     */
    val UltraThinBorderWidth: Dp = 0.5.dp

    /**
     * Default glass style for standard glossy surfaces.
     */
    val DefaultGlassStyle = GlassDefaults.regular

    /**
     * Compact glass style for subtle glossy elements.
     */
    val CompactGlassStyle = GlassDefaults.thin

    /**
     * Strong glass style for hero panels and modal surfaces.
     */
    val StrongGlassStyle = GlassDefaults.thick

    /**
     * Ultra-strong glass style for full-screen overlays.
     */
    val UltraStrongGlassStyle = GlassDefaults.ultraThick

    /**
     * Default glossy surface shape.
     */
    val DefaultShape: Shape = RoundedCornerShape(CornerRadius)

    /**
     * Compact glossy surface shape.
     */
    val CompactShape: Shape = RoundedCornerShape(CompactCornerRadius)

    /**
     * Pill shape for chips and tags.
     */
    val PillShape: Shape = CircleShape
}

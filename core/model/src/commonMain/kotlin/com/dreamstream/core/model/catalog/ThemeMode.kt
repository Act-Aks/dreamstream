package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/**
 * App-wide theme preference.
 *
 * [ThemeMode] controls the UI color scheme for DreamStream. It is used in
 * ***`AppConfig.themeMode`*** and ***`UserPreferences.themeMode`*** to persist
 * and apply the user's theme choice across the app.
 *
 * This enum provides three modes:
 * - **[Light]**: Always use light theme
 * - **[Dark]**: Always use dark theme
 * - **[System]**: Follow device system settings
 *
 * ## Key Properties:
 * - Name: Theme identifier (***required***)
 *
 * ## Theme Modes:
 * | Mode | Behavior |
 * |------|----------|
 * | [Light] | Always light background |
 * | [Dark] | Always dark background |
 * | [System] | Matches device setting |
 *
 * ## Usage:
 * ```kotlin
 * val theme = ThemeMode.Dark
 * // Apply theme to Compose Material Theme
 * MaterialTheme(
 *     colorScheme = if (theme == ThemeMode.Dark) darkColorScheme() else lightColorScheme()
 * )
 * ```
 *
 * ## In App Configuration:
 * ```kotlin
 * AppConfig(
 *     themeMode = ThemeMode.System,
 *    language = "en"
 * )
 * ```
 *
 * ## Related:
 * - Used in:
 *      ***`AppConfig`***
 *      ***`UserPreferences`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
enum class ThemeMode {
    /**
     * Always use light theme.
     *
     * ***Behavior***: Light background regardless of device settings.
     */
    Light,

    /**
     * Always use dark theme.
     *
     * ***Behavior***: Dark background regardless of device settings.
     */
    Dark,

    /**
     * Follow device system setting.
     *
     * ***Behavior***: Automatically switches between light/dark based on
     * the device's system-wide theme preference.
     */
    System,
}

package com.dreamstream.feature.settings.domain.model

/**
 * Supported app languages for DreamStream.
 *
 * [SYSTEM] follows the device locale. Display names for all entries except
 * SYSTEM are hardcoded in their native script so they always render correctly
 * regardless of the current app locale. SYSTEM's display name is resolved
 * from a string resource (translatable) in the presentation layer.
 */
enum class AppLanguage(val tag: String) {

    /** Follow the device locale — the default. */
    SYSTEM(tag = ""),

    /** English. */
    ENGLISH(tag = "en"),

    /** Hindi / हिंदी. */
    HINDI(tag = "hi"),

    /** German / Deutsch. */
    GERMAN(tag = "de"),

    /** Japanese / 日本語. */
    JAPANESE(tag = "ja");

    /** Display name in the language's own script. SYSTEM returns empty — use
     * [SettingsScreen] to resolve via string resource. */
    val displayName: String
        get() = when (this) {
            SYSTEM -> ""
            ENGLISH -> "English"
            HINDI -> "हिंदी"
            GERMAN -> "Deutsch"
            JAPANESE -> "日本語"
        }

    companion object {
        /** Look up an [AppLanguage] by its BCP 47 language tag.
         *  Returns [SYSTEM] for unknown or empty tags. */
        fun fromTag(tag: String): AppLanguage = entries.firstOrNull { it.tag == tag } ?: SYSTEM
    }
}

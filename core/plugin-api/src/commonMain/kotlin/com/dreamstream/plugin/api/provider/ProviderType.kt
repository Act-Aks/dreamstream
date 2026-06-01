package com.dreamstream.plugin.api.provider

/**
 * Type of provider based on streaming link delivery method.
 */
enum class ProviderType {
    /**
     * Provider returns direct video URLs (e.g., .mp4, .m3u8).
     *
     * Can be played directly without further extraction.
     */
    DirectLink,

    /**
     * Provider returns links to video hosts that need link extraction.
     *
     * Example: returns "https://vidstreaming.io/embed/xxx" which
     * must be scraped to extract the actual .mp4 URL.
     */
    MetaData,
}

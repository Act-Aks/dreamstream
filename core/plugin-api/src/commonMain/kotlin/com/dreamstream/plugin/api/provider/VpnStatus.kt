package com.dreamstream.plugin.api.provider

/**
 * VPN requirement status for a content provider.
 */
enum class VpnStatus {
    /** No VPN needed for this provider */
    None,

    /** VPN may help in some regions but isn't strictly required */
    MightBeNeeded,

    /** VPN is required; provider is blocked without it */
    Needed,
}

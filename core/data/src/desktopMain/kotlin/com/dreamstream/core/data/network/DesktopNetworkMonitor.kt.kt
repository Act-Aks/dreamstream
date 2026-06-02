package com.dreamstream.core.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class DesktopNetworkMonitor : NetworkMonitor {
    // Desktop: assume always online (simplified)
    override val isOnline: StateFlow<Boolean> = MutableStateFlow(true)
    override val networkState: Flow<NetworkState> = flowOf(NetworkState.Online)
}

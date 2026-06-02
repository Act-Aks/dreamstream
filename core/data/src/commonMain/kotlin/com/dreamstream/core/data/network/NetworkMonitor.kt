package com.dreamstream.core.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NetworkMonitor {
    val isOnline: StateFlow<Boolean>
    val networkState: Flow<NetworkState>
}

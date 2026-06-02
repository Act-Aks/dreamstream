package com.dreamstream.core.data.network

sealed interface NetworkState {
    data object Online : NetworkState
    data object Offline : NetworkState
    data class Limited(val reason: String) : NetworkState
}

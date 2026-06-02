package com.dreamstream.core.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Android implementation of [NetworkMonitor] that monitors network connectivity
 * using Android's [ConnectivityManager].
 *
 * **Required Permissions:**
 * Add the following permissions to your **app module's** `AndroidManifest.xml`:
 * ```xml
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.INTERNET" />
 * ```
 *
 * - `ACCESS_NETWORK_STATE`: Required to listen to network state changes via
 *   [ConnectivityManager.NetworkCallback]. This is a **normal permission** and
 *   does **not** require runtime request (even on `targetSdkVersion >= 23`).
 *
 * - `INTERNET`: Required for the app to access the internet. Also a normal permission.
 *
 * **Usage:**
 * ```kotlin
 * // In your app module or DI module
 * val networkMonitor: NetworkMonitor = AndroidNetworkMonitor(context)
 *
 * // Observe network state
 * lifecycleScope.launch {
 *     networkMonitor.networkState.collect { state ->
 *         when (state) {
 *             is NetworkState.Online -> showOnlineIndicator()
 *             is NetworkState.Offline -> showOfflineIndicator()
 *             is NetworkState.Limited -> showLimitedIndicator(state.reason)
 *         }
 *     }
 * }
 *
 * // Or just check if online
 * val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
 * ```
 *
 * **Implementation details:**
 * - Uses [callbackFlow] to convert [ConnectivityManager.NetworkCallback] into a [Flow]
 * - Emits current network state immediately upon creation
 * - Automatically unregisters callback when flow is cancelled
 * - Uses `WhileSubscribed(5_000)` to keep flowing for 5 seconds after last collector
 * - Properly scopes coroutines to avoid memory leaks (no `GlobalScope`)
 *
 * @param context Application or Activity context (application context is preferred to avoid leaks)
 */
class AndroidNetworkMonitor(private val context: Context) : NetworkMonitor {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val scope = CoroutineScope(SupervisorJob())

    @SuppressLint("MissingPermission") // Permissions are declared in the app module's manifest.
    private val _networkState = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkState.Online)
            }

            override fun onLost(network: Network) {
                trySend(NetworkState.Offline)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities,
            ) {
                val hasInternet = capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
                val hasValidated = capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(
                    if (hasInternet && hasValidated) NetworkState.Online
                    else NetworkState.Limited("No validated internet")
                )
            }
        }

        val request =
            NetworkRequest
                .Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Emit current state immediately
        val currentState =
            connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
                ?.let {
                    if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        NetworkState.Online
                    } else {
                        NetworkState.Limited("Not validated")
                    }
                } ?: NetworkState.Offline

        trySend(currentState)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NetworkState.Offline,
    )

    override val isOnline: StateFlow<Boolean> =
        _networkState.map { it is NetworkState.Online }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    override val networkState: Flow<NetworkState> = _networkState
}

package io.woong.filmpedia.util

import android.net.ConnectivityManager
import android.net.Network
import androidx.annotation.RequiresApi

@RequiresApi(21)
class NetworkStateCallback(
    private val onOnline: (network: Network) -> Unit,
    private val onOffline: (network: Network) -> Unit
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onOnline(network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        onOffline(network)
    }
}

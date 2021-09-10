package io.woong.filmpedia.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.util.NetworkStateCallback

@Suppress("unused")
abstract class BaseActivity<B : ViewDataBinding>(private val layoutRes: Int) : AppCompatActivity() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding!!

    private var _app: FilmpediaApp? = null
    protected val app: FilmpediaApp
        get() = _app!!

    protected val apiKey: String
        get() = app.tmdbApiKey
    protected val language: String
        get() = app.language
    protected val region: String
        get() = app.region

    private var networkCallback: NetworkStateCallback? = null
    private var isOnline: Boolean = false

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutRes)
        _app = application as FilmpediaApp
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 21) {
            registerNetworkCallback()
        }
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= 21) {
            unregisterNetworkCallback()
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        _app = null
        _binding?.unbind()
        _binding = null
    }

    @RequiresApi(21)
    private fun registerNetworkCallback() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        networkCallback = NetworkStateCallback(
            onOnline = {
                isOnline = true
            },
            onOffline = {
                isOnline = false
            }
        )

        networkCallback?.let {
            manager.registerNetworkCallback(request, it)
        }
    }

    @RequiresApi(21)
    private fun unregisterNetworkCallback() {
        networkCallback?.let {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            manager.unregisterNetworkCallback(it)
        }
    }

    @Suppress("DEPRECATION")
    protected fun checkIsOnline(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= 21) {
            isOnline
        } else {
            val info = manager.activeNetworkInfo
            info?.isConnectedOrConnecting ?: false
        }
    }

    protected fun log(message: String, priority: Int = Log.DEBUG) {
        val tag = this::class.simpleName
        Log.println(priority, tag, message)
    }

    protected fun toast(@StringRes message: Int, duration: Int = Toast.LENGTH_LONG) {
        val realMessage = getString(message)
        Toast.makeText(this, realMessage, duration).show()
    }

    protected fun toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, message, duration).show()
    }

    protected fun snackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        val realMessage = getString(message)
        Snackbar.make(this, binding.root, realMessage, duration).show()
    }

    protected fun snackbar(message: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, binding.root, message, duration).show()
    }
}

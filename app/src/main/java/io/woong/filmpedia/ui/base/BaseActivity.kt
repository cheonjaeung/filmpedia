package io.woong.filmpedia.ui.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import io.woong.filmpedia.FilmpediaApp

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

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutRes)
        _app = application as FilmpediaApp
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

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        _app = null
        _binding?.unbind()
        _binding = null
    }
}

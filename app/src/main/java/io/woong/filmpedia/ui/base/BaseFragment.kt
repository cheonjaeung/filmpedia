package io.woong.filmpedia.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.woong.filmpedia.FilmpediaApp

@Suppress("unused")
abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : Fragment() {

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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _app = activity?.application as FilmpediaApp
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _app = null
        _binding?.unbind()
        _binding = null
    }

    protected fun log(message: String, priority: Int = Log.DEBUG) {
        val tag = this::class.simpleName
        Log.println(priority, tag, message)
    }

    protected fun toast(@StringRes message: Int, duration: Int = Toast.LENGTH_LONG) {
        val realMessage = getString(message)
        Toast.makeText(requireContext(), realMessage, duration).show()
    }

    protected fun toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    protected fun snackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        val realMessage = getString(message)
        Snackbar.make(requireActivity(), binding.root, realMessage, duration).show()
    }

    protected fun snackbar(message: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(requireContext(), binding.root, message, duration).show()
    }
}

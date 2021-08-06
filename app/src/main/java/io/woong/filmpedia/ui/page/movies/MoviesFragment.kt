package io.woong.filmpedia.ui.page.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.woong.filmpedia.databinding.FragmentMoviesBinding

class MoviesFragment : Fragment() {

    private var mBinding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}

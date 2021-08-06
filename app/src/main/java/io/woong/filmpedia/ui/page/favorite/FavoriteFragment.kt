package io.woong.filmpedia.ui.page.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.woong.filmpedia.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var mBinding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}

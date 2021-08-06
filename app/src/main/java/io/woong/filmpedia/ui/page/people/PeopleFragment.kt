package io.woong.filmpedia.ui.page.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.woong.filmpedia.databinding.FragmentPeopleBinding

class PeopleFragment : Fragment() {

    private var mBinding: FragmentPeopleBinding? = null
    private val binding: FragmentPeopleBinding
        get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}

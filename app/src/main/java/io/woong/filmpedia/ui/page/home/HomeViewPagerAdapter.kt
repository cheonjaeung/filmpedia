package io.woong.filmpedia.ui.page.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.woong.filmpedia.ui.page.home.fragment.HighRatedMoviesFragment
import io.woong.filmpedia.ui.page.home.fragment.NowPlayingMoviesFragment
import io.woong.filmpedia.ui.page.home.fragment.PopularMoviesFragment

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularMoviesFragment()
            1 -> NowPlayingMoviesFragment()
            else -> HighRatedMoviesFragment()
        }
    }
}

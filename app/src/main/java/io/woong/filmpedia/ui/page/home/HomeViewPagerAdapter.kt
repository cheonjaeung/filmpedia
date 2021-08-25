package io.woong.filmpedia.ui.page.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

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

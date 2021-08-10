package io.woong.filmpedia.ui.page.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ActivityHomeBinding
import io.woong.filmpedia.ui.page.bookmarks.BookmarksFragment
import io.woong.filmpedia.ui.page.movies.MoviesFragment
import io.woong.filmpedia.ui.page.people.PeopleFragment

class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    companion object {
        private const val FRAGMENT_NOT_SELECTED: Int = -1
        private const val MOVIES_FRAGMENT_INDEX: Int = 0
        private const val PEOPLE_FRAGMENT_INDEX: Int = 1
        private const val BOOKMARKS_FRAGMENT_INDEX: Int = 2
    }

    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding
        get() = _binding!!

    private val fragments: Array<Fragment> = arrayOf(
        MoviesFragment(),
        PeopleFragment(),
        BookmarksFragment()
    )
    private var currentFragmentIndex: Int = FRAGMENT_NOT_SELECTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.navigation.setOnItemSelectedListener(this)
        switchFragment(MOVIES_FRAGMENT_INDEX)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_movies -> switchFragment(MOVIES_FRAGMENT_INDEX)
            R.id.navigation_people -> switchFragment(PEOPLE_FRAGMENT_INDEX)
            R.id.navigation_bookmarks -> switchFragment(BOOKMARKS_FRAGMENT_INDEX)
            else -> false
        }
    }

    private fun switchFragment(fragmentIndex: Int): Boolean {
        return if (fragmentIndex != currentFragmentIndex) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.frame.id, fragments[fragmentIndex])
                .commitAllowingStateLoss()

            currentFragmentIndex = fragmentIndex

            true
        } else {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

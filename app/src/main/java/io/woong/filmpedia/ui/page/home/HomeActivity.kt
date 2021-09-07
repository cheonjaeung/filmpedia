package io.woong.filmpedia.ui.page.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ActivityHomeBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.page.movie.MovieActivity
import io.woong.filmpedia.ui.page.search.SearchActivity
import io.woong.filmpedia.util.AnimationUtil

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@HomeActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
            }

            viewPager.apply {
                adapter = HomeViewPagerAdapter(this@HomeActivity)
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val tabNames = arrayOf(
                    resources.getString(R.string.home_tab_0),
                    resources.getString(R.string.home_tab_1),
                    resources.getString(R.string.home_tab_2)
                )
                tab.text = tabNames[position]
            }.attach()

            AnimationUtil.blink(loading, 1000)
        }

        viewModel.updateAll(apiKey, language, region)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_movies_search) {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun startMovieActivity(movieTitle: String, movieId: Int) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra(MovieActivity.MOVIE_TITLE_EXTRA_ID, movieTitle)
        intent.putExtra(MovieActivity.MOVIE_ID_EXTRA_ID, movieId)
        startActivity(intent)
    }
}

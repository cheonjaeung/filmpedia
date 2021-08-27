package io.woong.filmpedia.ui.page.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ActivityHomeBinding
import io.woong.filmpedia.ui.page.moviedetail.MovieDetailActivity
import io.woong.filmpedia.ui.page.search.SearchActivity

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

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
        }

        val app = application as FilmpediaApp
        viewModel.updateAll(app.tmdbApiKey, app.language, app.region)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_movies_toolbar, menu)
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

    fun startMovieDetailActivity(movieTitle: String, movieId: Int) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.MOVIE_TITLE_EXTRA_ID, movieTitle)
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA_ID, movieId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
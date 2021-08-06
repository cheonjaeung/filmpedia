package io.woong.filmpedia.ui.page.home

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationBarView
import io.woong.filmpedia.R
import io.woong.filmpedia.adapter.Top10MovieListAdapter
import io.woong.filmpedia.databinding.ActivityHomeBinding
import io.woong.filmpedia.ui.page.favorite.FavoriteFragment
import io.woong.filmpedia.ui.page.movies.MoviesFragment
import io.woong.filmpedia.ui.page.people.PeopleFragment
import io.woong.filmpedia.util.HorizontalItemDecoration
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val MOVIES_FRAGMENT_INDEX: Int = 0
        private const val PEOPLE_FRAGMENT_INDEX: Int = 1
        private const val FAVORITE_FRAGMENT_INDEX: Int = 2
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private val fragments: Array<Fragment> = arrayOf(
        MoviesFragment(),
        PeopleFragment(),
        FavoriteFragment()
    )
    private var currentFragmentIndex: Int = MOVIES_FRAGMENT_INDEX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

//        viewModel.recommendedMovie.observe(this) { movie ->
//            binding.recommendedMovie.movie = movie
//        }
//
//        binding.homeSwipeLayout.setOnRefreshListener(this)
//
//        val listDeco = HorizontalItemDecoration(8, resources.displayMetrics)
//
//        binding.top10NowPlayingList.apply {
//            adapter = Top10MovieListAdapter(this@HomeActivity)
//            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(listDeco)
//        }
//        viewModel.top10NowPlayingMovies.observe(this) { movies ->
//            val adapter = binding.top10NowPlayingList.adapter as Top10MovieListAdapter
//            adapter.setTop10(movies)
//        }
//
//        binding.top10PopularList.apply {
//            adapter = Top10MovieListAdapter(this@HomeActivity)
//            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(listDeco)
//        }
//        viewModel.top10PopularMovies.observe(this) { movies ->
//            val adapter = binding.top10PopularList.adapter as Top10MovieListAdapter
//            adapter.setTop10(movies)
//        }
//
//        binding.top10RatedList.apply {
//            adapter = Top10MovieListAdapter(this@HomeActivity)
//            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(listDeco)
//        }
//        viewModel.top10RatedMovies.observe(this) { movies ->
//            val adapter = binding.top10RatedList.adapter as Top10MovieListAdapter
//            adapter.setTop10(movies)
//        }
//
//        binding.top10UpcomingList.apply {
//            adapter = Top10MovieListAdapter(this@HomeActivity)
//            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(listDeco)
//        }
//        viewModel.top10UpcomingMovies.observe(this) { movies ->
//            val adapter = binding.top10UpcomingList.adapter as Top10MovieListAdapter
//            adapter.setTop10(movies)
//        }
//
//        viewModel.update()
    }

    private fun initViews() {
        binding.homeNavigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_movies -> switchFragment(MOVIES_FRAGMENT_INDEX)
            R.id.navigation_people -> switchFragment(PEOPLE_FRAGMENT_INDEX)
            R.id.navigation_favorite -> switchFragment(FAVORITE_FRAGMENT_INDEX)
        }

        return true
    }

    private fun switchFragment(fragmentIndex: Int) {
        if (fragmentIndex != currentFragmentIndex) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.homeFrame.id, fragments[fragmentIndex])
                .commit()

            currentFragmentIndex = fragmentIndex
        }
    }

    override fun onRefresh() {
        viewModel.isLoading.observe(this) {
            if (it == false) {
                binding.homeSwipeLayout.isRefreshing = it
            }
        }
        viewModel.update()
    }
}

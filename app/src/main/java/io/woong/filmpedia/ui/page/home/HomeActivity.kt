package io.woong.filmpedia.ui.page.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.woong.filmpedia.adapter.Top10MovieListAdapter
import io.woong.filmpedia.databinding.ActivityHomeBinding
import io.woong.filmpedia.util.HorizontalItemDecoration
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.recommendedMovie.observe(this) { movie ->
            binding.recommendedMovie.movie = movie
        }

        binding.homeSwipeLayout.setOnRefreshListener(this)

        val listDeco = HorizontalItemDecoration(8, resources.displayMetrics)

        binding.top10NowPlayingList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10NowPlayingMovies.observe(this) { movies ->
            val adapter = binding.top10NowPlayingList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10PopularList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10PopularMovies.observe(this) { movies ->
            val adapter = binding.top10PopularList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10RatedList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10RatedMovies.observe(this) { movies ->
            val adapter = binding.top10RatedList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10UpcomingList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10UpcomingMovies.observe(this) { movies ->
            val adapter = binding.top10UpcomingList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        viewModel.update()
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

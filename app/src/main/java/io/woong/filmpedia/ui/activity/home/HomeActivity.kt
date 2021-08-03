package io.woong.filmpedia.ui.activity.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.woong.filmpedia.adapter.Top10MovieListAdapter
import io.woong.filmpedia.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.recommendedMovie.observe(this) { movie ->
            binding.recommendedMovie.movie = movie
        }

        binding.top10NowPlayingList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.top10NowPlayingMovies.observe(this) { movies ->
            val adapter = binding.top10NowPlayingList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10PopularList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.top10PopularMovies.observe(this) { movies ->
            val adapter = binding.top10PopularList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10RatedList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.top10RatedMovies.observe(this) { movies ->
            val adapter = binding.top10RatedList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10UpcomingList.apply {
            adapter = Top10MovieListAdapter(this@HomeActivity)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.top10UpcomingMovies.observe(this) { movies ->
            val adapter = binding.top10UpcomingList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        viewModel.update()
    }
}

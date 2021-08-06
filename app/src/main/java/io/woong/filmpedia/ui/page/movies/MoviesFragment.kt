package io.woong.filmpedia.ui.page.movies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.woong.filmpedia.adapter.Top10MovieListAdapter
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.databinding.FragmentMoviesBinding
import io.woong.filmpedia.ui.component.MovieDetailBottomSheet
import io.woong.filmpedia.ui.component.RecommendedMovieView
import io.woong.filmpedia.util.HorizontalItemDecoration
import java.lang.NullPointerException

class MoviesFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    RecommendedMovieView.OnInfoButtonClickListener,
    Top10MovieListAdapter.OnItemClickListener {

    private val viewModel: MoviesViewModel by viewModels()
    private var mBinding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        
        val context: Context = container?.context ?: throw NullPointerException("Context cannot be null.")

        binding.recommendedMovie.setOnInfoButtonClickListener(this)
        viewModel.recommendedMovie.observe(viewLifecycleOwner) { movie ->
            binding.recommendedMovie.movie = movie
        }

        binding.swipeLayout.setOnRefreshListener(this)

        val listDeco = HorizontalItemDecoration(8, resources.displayMetrics)

        binding.top10NowPlayingList.apply {
            adapter = Top10MovieListAdapter(context).apply {
                setOnItemClickListener(this@MoviesFragment)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10NowPlayingMovies.observe(viewLifecycleOwner) { movies ->
            val adapter = binding.top10NowPlayingList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10PopularList.apply {
            adapter = Top10MovieListAdapter(context).apply {
                setOnItemClickListener(this@MoviesFragment)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10PopularMovies.observe(viewLifecycleOwner) { movies ->
            val adapter = binding.top10PopularList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10RatedList.apply {
            adapter = Top10MovieListAdapter(context).apply {
                setOnItemClickListener(this@MoviesFragment)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10RatedMovies.observe(viewLifecycleOwner) { movies ->
            val adapter = binding.top10RatedList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        binding.top10UpcomingList.apply {
            adapter = Top10MovieListAdapter(context).apply {
                setOnItemClickListener(this@MoviesFragment)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(listDeco)
        }
        viewModel.top10UpcomingMovies.observe(viewLifecycleOwner) { movies ->
            val adapter = binding.top10UpcomingList.adapter as Top10MovieListAdapter
            adapter.setTop10(movies)
        }

        viewModel.update()

        return binding.root
    }

    override fun onRefresh() {
        viewModel.update()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == false) {
                binding.swipeLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onInfoButtonClick(view: RecommendedMovieView, movie: RecommendedMovie?) {
        if (view.id == binding.recommendedMovie.id) {
            movie?.let { m ->
                val sheet = MovieDetailBottomSheet(m.movie)
                sheet.show(parentFragmentManager, "MovieDetailBottomSheet")
            }
        }
    }

    override fun onItemClick(position: Int, movies: List<Movies.Result>) {
        if (position != RecyclerView.NO_POSITION) {
            val sheet = MovieDetailBottomSheet(movies[position])
            sheet.show(parentFragmentManager, "MovieDetailBottomSheet")
        }
    }
}

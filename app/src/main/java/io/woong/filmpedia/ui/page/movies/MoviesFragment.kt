package io.woong.filmpedia.ui.page.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.woong.filmpedia.R
import io.woong.filmpedia.adapter.Top10MovieListAdapter
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.databinding.FragmentMoviesBinding
import io.woong.filmpedia.ui.component.MovieDetailBottomSheet
import io.woong.filmpedia.ui.component.RecommendedMovieView
import io.woong.filmpedia.ui.page.moviedetail.MovieDetailActivity
import io.woong.filmpedia.util.HorizontalItemDecoration
import java.lang.NullPointerException

class MoviesFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    RecommendedMovieView.OnImageClickListener,
    RecommendedMovieView.OnInfoButtonClickListener,
    Top10MovieListAdapter.OnItemClickListener,
    MovieDetailBottomSheet.OnDetailButtonClickListener{

    private val viewModel: MoviesViewModel by viewModels()
    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        
        val context: Context = container?.context ?: throw NullPointerException("Context cannot be null.")

        binding.apply {
            lifecycleOwner = this@MoviesFragment
            vm = viewModel

            swipeLayout.setOnRefreshListener(this@MoviesFragment)

            recommendedMovie.apply {
                setOnImageClickListener(this@MoviesFragment)
                setOnInfoButtonClickListener(this@MoviesFragment)
            }

            val itemDeco = HorizontalItemDecoration(8, resources.displayMetrics)
            top10NowPlayingList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesFragment)
                    setRatingEnabled(false)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            top10PopularList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesFragment)
                    setRatingEnabled(false)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            top10RatedList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesFragment)
                    setRatingEnabled(true)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            top10UpcomingList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesFragment)
                    setRatingEnabled(false)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }
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

    override fun onImageClickListener(view: RecommendedMovieView, movie: RecommendedMovie?) {
        if (view.id == binding.recommendedMovie.id) {
            movie?.let { m ->
                startMovieDetailActivity(m.movie.id)
            }
        }
    }

    override fun onInfoButtonClick(view: RecommendedMovieView, movie: RecommendedMovie?) {
        if (view.id == binding.recommendedMovie.id) {
            movie?.let { m ->
                openMovieDetailBottomSheet(m.movie)
            }
        }
    }

    override fun onItemClick(position: Int, movies: List<Movies.Result>) {
        if (position != RecyclerView.NO_POSITION) {
            openMovieDetailBottomSheet(movies[position])
        }
    }

    private fun openMovieDetailBottomSheet(movie: Movies.Result) {
        val sheet = MovieDetailBottomSheet(movie)
        sheet.apply {
            setOnDetailButtonClickListener(this@MoviesFragment)
            show(this@MoviesFragment.parentFragmentManager, sheet::class.java.simpleName)
        }
    }

    override fun onDetailButtonClick(movie: Movies.Result) {
        startMovieDetailActivity(movie.id)
    }

    private fun startMovieDetailActivity(movieId: Int) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA_ID, movieId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@BindingAdapter("movies_recommended_movie")
fun RecommendedMovieView.bindRecommendedMovie(movie: RecommendedMovie?) {
    movie?.let { m ->
        this.movie = m
    }
}

@BindingAdapter("movies_top10_movies")
fun RecyclerView.bindTop10Movies(movies: List<Movies.Result>?) {
    movies?.let { m ->
        val adapter = this.adapter as Top10MovieListAdapter
        adapter.setTop10(m)
    }
}

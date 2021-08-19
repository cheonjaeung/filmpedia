package io.woong.filmpedia.ui.page.movies

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.adapter.Top10MovieListAdapter
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.databinding.ActivityMoviesBinding
import io.woong.filmpedia.ui.component.MovieDetailBottomSheet
import io.woong.filmpedia.ui.component.RecommendedMovieView
import io.woong.filmpedia.ui.page.moviedetail.MovieDetailActivity
import io.woong.filmpedia.util.HorizontalItemDecoration

class MoviesActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener,
    RecommendedMovieView.OnImageClickListener,
    RecommendedMovieView.OnInfoButtonClickListener,
    Top10MovieListAdapter.OnItemClickListener,
    MovieDetailBottomSheet.OnDetailButtonClickListener {

    private val viewModel: MoviesViewModel by viewModels()
    private var _binding: ActivityMoviesBinding? = null
    private val binding: ActivityMoviesBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)

        binding.apply {
            lifecycleOwner = this@MoviesActivity
            vm = viewModel

            swipeLayout.setOnRefreshListener(this@MoviesActivity)

            recommendedMovie.apply {
                setOnImageClickListener(this@MoviesActivity)
                setOnInfoButtonClickListener(this@MoviesActivity)
            }

            val itemDeco = HorizontalItemDecoration(8, resources.displayMetrics)
            top10NowPlayingList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesActivity)
                    setRatingEnabled(false)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            top10PopularList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesActivity)
                    setRatingEnabled(false)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            top10RatedList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesActivity)
                    setRatingEnabled(true)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            top10UpcomingList.apply {
                adapter = Top10MovieListAdapter(context).apply {
                    setOnItemClickListener(this@MoviesActivity)
                    setRatingEnabled(false)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }
        }

        update()
    }

    override fun onRefresh() {
        update()
        viewModel.isLoading.observe(this) {
            if (it == false) {
                binding.swipeLayout.isRefreshing = false
            }
        }
    }

    private fun update() {
        val app = application as FilmpediaApp
        viewModel.update(app.tmdbApiKey, app.language, app.region)
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

    override fun onItemClick(position: Int, movies: List<Movies.Movie>) {
        if (position != RecyclerView.NO_POSITION) {
            openMovieDetailBottomSheet(movies[position])
        }
    }

    private fun openMovieDetailBottomSheet(movie: Movies.Movie) {
        val sheet = MovieDetailBottomSheet(movie)
        sheet.apply {
            setOnDetailButtonClickListener(this@MoviesActivity)
            show(this@MoviesActivity.supportFragmentManager, sheet::class.java.simpleName)
        }
    }

    override fun onDetailButtonClick(movie: Movies.Movie) {
        startMovieDetailActivity(movie.id)
    }

    private fun startMovieDetailActivity(movieId: Int) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA_ID, movieId)
        startActivity(intent)
    }
}

@BindingAdapter("movies_recommended_movie")
fun RecommendedMovieView.bindRecommendedMovie(movie: RecommendedMovie?) {
    movie?.let { m ->
        this.movie = m
    }
}

@BindingAdapter("movies_top10_movies")
fun RecyclerView.bindTop10Movies(movies: List<Movies.Movie>?) {
    movies?.let { m ->
        val adapter = this.adapter as Top10MovieListAdapter
        adapter.setTop10(m)
    }
}


package io.woong.filmpedia.ui.page.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.databinding.FragmentNowPlayingMoviesBinding
import io.woong.filmpedia.ui.page.home.HomeViewModel
import io.woong.filmpedia.ui.page.home.MovieListAdapter
import io.woong.filmpedia.util.InfinityScrollListener
import io.woong.filmpedia.util.ListDecoration

class NowPlayingMoviesFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentNowPlayingMoviesBinding? = null
    private val binding: FragmentNowPlayingMoviesBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNowPlayingMoviesBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@NowPlayingMoviesFragment
            vm = viewModel

            movieList.apply {
                val columnSize = 3
                adapter = MovieListAdapter().apply {
                    headerTitle = resources.getString(R.string.home_title_now_playing)
                }
                setHasFixedSize(true)
                val lm = GridLayoutManager(activity, columnSize, GridLayoutManager.VERTICAL, false)
                lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) {
                            columnSize
                        } else {
                            1
                        }
                    }
                }
                layoutManager = lm
                addItemDecoration(ListDecoration.GridWithHeaderDecoration(columnSize, 2))
                addOnScrollListener(
                    InfinityScrollListener {
                        val app = activity?.application as FilmpediaApp
                        viewModel.updateNowPlaying(app.tmdbApiKey, app.language, app.region)
                    }
                )
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object BindingAdapters {
        @JvmStatic
        @BindingAdapter("now_playing_movies")
        fun bindNowPlayingMovies(view: RecyclerView, movies: List<Movies.Movie>?) {
            if (movies != null && movies.isNotEmpty()) {
                val adapter = view.adapter as MovieListAdapter
                adapter.items = movies
            }
        }
    }
}

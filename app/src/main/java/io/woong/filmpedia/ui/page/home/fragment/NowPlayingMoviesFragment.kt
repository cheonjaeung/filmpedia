package io.woong.filmpedia.ui.page.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.databinding.FragmentNowPlayingMoviesBinding
import io.woong.filmpedia.ui.base.BaseFragment
import io.woong.filmpedia.ui.page.home.HomeActivity
import io.woong.filmpedia.ui.page.home.HomeViewModel
import io.woong.filmpedia.ui.page.home.MovieListAdapter
import io.woong.filmpedia.util.GoToTopScrollListener
import io.woong.filmpedia.util.InfinityScrollListener
import io.woong.filmpedia.util.ListDecoration

class NowPlayingMoviesFragment : BaseFragment<FragmentNowPlayingMoviesBinding>(R.layout.fragment_now_playing_movies),
    MovieListAdapter.OnMovieListItemClickListener {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@NowPlayingMoviesFragment
            vm = viewModel

            movieList.apply {
                val columnSize = 3
                adapter = MovieListAdapter().apply {
                    headerTitle = resources.getString(R.string.home_title_now_playing)
                    listener = this@NowPlayingMoviesFragment
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
                addItemDecoration(ListDecoration.GridWithHeaderDecoration(2))
                addOnScrollListener(
                    InfinityScrollListener {
                        val app = activity?.application as FilmpediaApp
                        viewModel.updateNowPlaying(app.tmdbApiKey, app.language, app.region)
                    }
                )
                addOnScrollListener(
                    GoToTopScrollListener(goToTopButton, 30, 2000) {
                        binding.movieList.smoothScrollToPosition(0)
                    }
                )
            }
        }

        return binding.root
    }

    override fun onMovieListItemClick(item: Movies.Movie) {
        if (activity is HomeActivity) {
            (activity as HomeActivity).startMovieActivity(item.title, item.id)
        }
    }
}

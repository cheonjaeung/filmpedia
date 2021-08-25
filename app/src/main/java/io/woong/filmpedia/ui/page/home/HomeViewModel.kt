package io.woong.filmpedia.ui.page.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

    private var popularPage: Int = 1
    private val _popularMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val popularMovies: LiveData<MutableList<Movies.Movie>>
        get() = _popularMovies

    private var nowPlayingPage: Int = 1
    private val _nowPlayingMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val nowPlayingMovies: LiveData<MutableList<Movies.Movie>>
        get() = _nowPlayingMovies

    private var highRatedPage: Int = 1
    private val _highRatedMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val highRatedMovies: LiveData<MutableList<Movies.Movie>>
        get() = _highRatedMovies

    fun updateAll(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        val popularJob = repository.fetchPopularMovies(apiKey, 1, language, region) { movies ->
            if (movies != null) {
                val list = movies.results
                if (list.isNotEmpty()) {
                    _popularMovies.postValue(list.toMutableList())
                }
            }
        }

        val nowPlayingJob = repository.fetchNowPlayingMovies(apiKey, 1, language, region) { movies ->
            if (movies != null) {
                val list = movies.results
                if (list.isNotEmpty()) {
                    _nowPlayingMovies.postValue(list.toMutableList())
                }
            }
        }

        val highRatedJob = repository.fetchHighRatedMovies(apiKey, 1, language, region) { movies ->
            if (movies != null) {
                val list = movies.results
                if (list.isNotEmpty()) {
                    _highRatedMovies.postValue(list.toMutableList())
                }
            }
        }

        joinAll(popularJob, nowPlayingJob, highRatedJob)
    }

    fun updatePopular(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        val nextPage = popularPage + 1

        repository.fetchPopularMovies(apiKey, nextPage, language, region) {}.join()

        popularPage = nextPage
    }

    fun updateNowPlaying(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        val nextPage = nowPlayingPage + 1

        repository.fetchNowPlayingMovies(apiKey, nextPage, language, region) {}.join()

        nowPlayingPage = nextPage
    }

    fun updateHighRated(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        val nextPage = highRatedPage + 1

        repository.fetchHighRatedMovies(apiKey, nextPage, language, region) {}.join()

        highRatedPage = nextPage
    }
}

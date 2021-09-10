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

    private val _isOffline: MutableLiveData<Boolean> = MutableLiveData(false)
    val isOffline: LiveData<Boolean>
        get() = _isOffline

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var popularPage: Int = 1
    private var isPopularLoading: Boolean = false
    private val _popularMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val popularMovies: LiveData<MutableList<Movies.Movie>>
        get() = _popularMovies

    private var nowPlayingPage: Int = 1
    private var isNowPlayingLoading: Boolean = false
    private val _nowPlayingMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val nowPlayingMovies: LiveData<MutableList<Movies.Movie>>
        get() = _nowPlayingMovies

    private var highRatedPage: Int = 1
    private var isHighRatedLoading: Boolean = false
    private val _highRatedMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val highRatedMovies: LiveData<MutableList<Movies.Movie>>
        get() = _highRatedMovies

    fun updateAll(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (!isPopularLoading && !isNowPlayingLoading && !isHighRatedLoading) {
            _isLoading.postValue(true)
            isPopularLoading = true
            isNowPlayingLoading = true
            isHighRatedLoading = true

            val popularJob = repository.fetchPopularMovies(apiKey, 1, language, region) { result ->
                result.onSuccess {
                    val list = it.results
                    if (list.isNotEmpty()) {
                        _popularMovies.postValue(list.toMutableList())
                    }
                }.onNetworkError {
                    _isOffline.postValue(true)
                }
            }

            val nowPlayingJob = repository.fetchNowPlayingMovies(apiKey, 1, language, region) { result ->
                result.onSuccess {
                    val list = it.results
                    if (list.isNotEmpty()) {
                        _nowPlayingMovies.postValue(list.toMutableList())
                    }
                }.onNetworkError {
                    _isOffline.postValue(true)
                }
            }

            val highRatedJob = repository.fetchHighRatedMovies(apiKey, 1, language, region) { result ->
                result.onSuccess {
                    val list = it.results
                    if (list.isNotEmpty()) {
                        _highRatedMovies.postValue(list.toMutableList())
                    }
                }.onNetworkError {
                    _isOffline.postValue(true)
                }
            }

            joinAll(popularJob, nowPlayingJob, highRatedJob)

            isPopularLoading = false
            isNowPlayingLoading = false
            isHighRatedLoading = false
            _isLoading.postValue(false)
        }
    }

    fun updatePopular(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (!isPopularLoading) {
            isPopularLoading = true
            val nextPage = popularPage + 1

            repository.fetchPopularMovies(apiKey, nextPage, language, region) { result ->
                result.onSuccess {
                    val newList = it.results
                    val currentList = popularMovies.value
                    currentList?.addAll(newList)
                    _popularMovies.postValue(currentList)
                }.onNetworkError {
                    _isOffline.postValue(true)
                }
            }.join()

            popularPage = nextPage
            isPopularLoading = false
        }
    }

    fun updateNowPlaying(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (!isNowPlayingLoading) {
            isNowPlayingLoading = true
            val nextPage = nowPlayingPage + 1

            repository.fetchNowPlayingMovies(apiKey, nextPage, language, region) { result ->
                result.onSuccess {
                    val newList = it.results
                    val currentList = nowPlayingMovies.value
                    currentList?.addAll(newList)
                    _nowPlayingMovies.postValue(currentList)
                }.onNetworkError {
                    _isOffline.postValue(true)
                }
            }.join()

            nowPlayingPage = nextPage
            isNowPlayingLoading = false
        }
    }

    fun updateHighRated(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (!isHighRatedLoading) {
            isHighRatedLoading = true
            val nextPage = highRatedPage + 1

            repository.fetchHighRatedMovies(apiKey, nextPage, language, region) { result ->
                result.onSuccess {
                    val newList = it.results
                    val currentList = highRatedMovies.value
                    currentList?.addAll(newList)
                    _highRatedMovies.postValue(currentList)
                }.onNetworkError {
                    _isOffline.postValue(true)
                }
            }.join()

            highRatedPage = nextPage
            isHighRatedLoading = false
        }
    }
}

package io.woong.filmpedia.ui.page.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

    private val _isOnline: MutableLiveData<Boolean> = MutableLiveData(true)
    val isOnline: LiveData<Boolean>
        get() = _isOnline

    private var popularPage: Int = 1
    private val _isPopularLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPopularLoading: LiveData<Boolean>
        get() = _isPopularLoading
    private val _popularMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val popularMovies: LiveData<MutableList<Movies.Movie>>
        get() = _popularMovies

    private var nowPlayingPage: Int = 1
    private val _isNowPlayingLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNowPlayingLoading: LiveData<Boolean>
        get() = _isNowPlayingLoading
    private val _nowPlayingMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val nowPlayingMovies: LiveData<MutableList<Movies.Movie>>
        get() = _nowPlayingMovies

    private var highRatedPage: Int = 1
    private val _isHighRatedLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isHighRatedLoading: LiveData<Boolean>
        get() = _isHighRatedLoading
    private val _highRatedMovies: MutableLiveData<MutableList<Movies.Movie>> = MutableLiveData()
    val highRatedMovies: LiveData<MutableList<Movies.Movie>>
        get() = _highRatedMovies

    fun updateAll(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (isPopularLoading.value != true && isNowPlayingLoading.value != true && isHighRatedLoading.value != true) {
            _isOnline.postValue(true)

            _isPopularLoading.postValue(true)
            _isNowPlayingLoading.postValue(true)
            _isHighRatedLoading.postValue(true)

            repository.fetchPopularMovies(apiKey, 1, language, region) { result ->
                result.onSuccess {
                    val list = it.results
                    if (list.isNotEmpty()) {
                        _popularMovies.postValue(list.toMutableList())
                    }
                }.onNetworkError {
                    _isOnline.postValue(false)
                }

                _isPopularLoading.postValue(false)
            }

            repository.fetchNowPlayingMovies(apiKey, 1, language, region) { result ->
                result.onSuccess {
                    val list = it.results
                    if (list.isNotEmpty()) {
                        _nowPlayingMovies.postValue(list.toMutableList())
                    }
                }.onNetworkError {
                    _isOnline.postValue(false)
                }

                _isNowPlayingLoading.postValue(false)
            }

            repository.fetchHighRatedMovies(apiKey, 1, language, region) { result ->
                result.onSuccess {
                    val list = it.results
                    if (list.isNotEmpty()) {
                        _highRatedMovies.postValue(list.toMutableList())
                    }
                }.onNetworkError {
                    _isOnline.postValue(false)
                }

                _isHighRatedLoading.postValue(false)
            }
        }
    }

    fun updatePopular(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (isPopularLoading.value != true) {
            _isOnline.postValue(true)

            _isPopularLoading.postValue(true)

            val nextPage = popularPage + 1

            repository.fetchPopularMovies(apiKey, nextPage, language, region) { result ->
                result.onSuccess {
                    val newList = it.results
                    val currentList = popularMovies.value
                    currentList?.addAll(newList)
                    _popularMovies.postValue(currentList)
                }.onNetworkError {
                    _isOnline.postValue(false)
                }

                popularPage = nextPage
                _isPopularLoading.postValue(false)
            }
        }
    }

    fun updateNowPlaying(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (isNowPlayingLoading.value != true) {
            _isOnline.postValue(true)

            _isNowPlayingLoading.postValue(true)

            val nextPage = nowPlayingPage + 1

            repository.fetchNowPlayingMovies(apiKey, nextPage, language, region) { result ->
                result.onSuccess {
                    val newList = it.results
                    val currentList = nowPlayingMovies.value
                    currentList?.addAll(newList)
                    _nowPlayingMovies.postValue(currentList)
                }.onNetworkError {
                    _isOnline.postValue(false)
                }

                nowPlayingPage = nextPage
                _isNowPlayingLoading.postValue(false)
            }
        }
    }

    fun updateHighRated(
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (isHighRatedLoading.value != true) {
            _isOnline.postValue(true)

            _isHighRatedLoading.postValue(true)

            val nextPage = highRatedPage + 1

            repository.fetchHighRatedMovies(apiKey, nextPage, language, region) { result ->
                result.onSuccess {
                    val newList = it.results
                    val currentList = highRatedMovies.value
                    currentList?.addAll(newList)
                    _highRatedMovies.postValue(currentList)
                }.onNetworkError {
                    _isOnline.postValue(false)
                }

                highRatedPage = nextPage
                _isHighRatedLoading.postValue(false)
            }
        }
    }
}

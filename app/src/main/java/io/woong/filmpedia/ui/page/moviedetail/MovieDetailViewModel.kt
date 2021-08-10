package io.woong.filmpedia.ui.page.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Movie
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _movie: MutableLiveData<Movie> = MutableLiveData()
    val movie: LiveData<Movie>
        get() = _movie

    fun update(movieId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            repository.fetchMovieDetail(movieId = movieId) { movie ->
                movie?.let { m ->
                    _movie.postValue(m)
                }
            }.join()

            _isLoading.postValue(false)
        }
    }
}

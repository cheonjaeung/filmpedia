package io.woong.filmpedia.ui.page.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Credits
import io.woong.filmpedia.data.Movie
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _movie: MutableLiveData<Movie> = MutableLiveData()
    val movie: LiveData<Movie>
        get() = _movie

    private val _credits: MutableLiveData<Credits> = MutableLiveData()
    val credits: LiveData<Credits>
        get() = _credits

    fun update(movieId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            val detailFetchingJob = repository.fetchMovieDetail(movieId = movieId) { movie ->
                movie?.let { m ->
                    _movie.postValue(m)
                }
            }

            val creditsFetchingJob = repository.fetchCredits(movieId = movieId) { credits ->
                credits?.let { c ->
                    _credits.postValue(c)
                }
            }

            joinAll(detailFetchingJob, creditsFetchingJob)

            _isLoading.postValue(false)
        }
    }
}

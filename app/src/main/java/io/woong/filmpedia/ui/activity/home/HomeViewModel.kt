package io.woong.filmpedia.ui.activity.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.repository.HomeRepository

class HomeViewModel : ViewModel() {

    private val repository: HomeRepository = HomeRepository()

    val recommendedMovie: MutableLiveData<RecommendedMovie> by lazy {
        MutableLiveData<RecommendedMovie>()
    }

    val nowPlayingMovies: MutableLiveData<List<Movies.Result>> by lazy {
        MutableLiveData<List<Movies.Result>>()
    }

    fun update() {
        repository.fetchRecommendedMovie { isSuccess, movie, error ->
            if (isSuccess) {
                recommendedMovie.value = movie
            }
        }

        repository.fetchNowPlaying10Movies { isSuccess, movies, error ->
            if (isSuccess) {
                nowPlayingMovies.value = movies
            }
        }
    }
}

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

    val top10NowPlayingMovies: MutableLiveData<List<Movies.Result>> by lazy {
        MutableLiveData<List<Movies.Result>>()
    }

    val top10PopularMovies: MutableLiveData<List<Movies.Result>> by lazy {
        MutableLiveData<List<Movies.Result>>()
    }

    val top10RatedMovies: MutableLiveData<List<Movies.Result>> by lazy {
        MutableLiveData<List<Movies.Result>>()
    }

    val top10UpcomingMovies: MutableLiveData<List<Movies.Result>> by lazy {
        MutableLiveData<List<Movies.Result>>()
    }

    fun update() {
        repository.fetchRecommendedMovie { isSuccess, movie, error ->
            if (isSuccess) {
                recommendedMovie.value = movie
            }
        }

        repository.fetchTop10NowPlayingMovies { isSuccess, movies, error ->
            if (isSuccess) {
                top10NowPlayingMovies.value = movies
            }
        }

        repository.fetchTop10PopularMovies { isSuccess, movies, error ->
            if (isSuccess) {
                top10PopularMovies.value = movies
            }
        }

        repository.fetchTop10RatedMovies { isSuccess, movies, error ->
            if (isSuccess) {
                top10RatedMovies.value = movies
            }
        }

        repository.fetchTop10UpcomingMovies { isSuccess, movies, error ->
            if (isSuccess) {
                top10UpcomingMovies.value = movies
            }
        }
    }
}

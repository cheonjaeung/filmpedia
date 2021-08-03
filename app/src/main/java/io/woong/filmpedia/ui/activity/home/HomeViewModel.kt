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
        repository.fetchRecommendedMovie { movie ->
            if (movie != null) {
                recommendedMovie.postValue(movie)
            }
        }

        repository.fetchTop10NowPlayingMovies { movies ->
            if (movies != emptyList<Movies.Result>()) {
                top10NowPlayingMovies.postValue(movies)
            }
        }

        repository.fetchTop10PopularMovies { movies ->
            if (movies != emptyList<Movies.Result>()) {
                top10PopularMovies.postValue(movies)
            }
        }

        repository.fetchTop10HighRateMovies { movies ->
            if (movies != emptyList<Movies.Result>()) {
                top10RatedMovies.postValue(movies)
            }
        }

        repository.fetchTop10UpcomingMovies { movies ->
            if (movies != emptyList<Movies.Result>()) {
                top10UpcomingMovies.postValue(movies)
            }
        }
    }
}

package io.woong.filmpedia.ui.page.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Genre
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {

    companion object {
        private const val NOW_PLAYING_MOVIES: Int = 1
        private const val POPULAR_MOVIES: Int = 2
        private const val TOP_RATED_MOVIES: Int = 3
        private const val UPCOMING_MOVIES: Int = 4
    }

    private val repository: MovieRepository = MovieRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _recommendedMovie: MutableLiveData<RecommendedMovie> = MutableLiveData()
    val recommendedMovie: LiveData<RecommendedMovie>
        get() = _recommendedMovie

    private val _top10NowPlayingMovies: MutableLiveData<List<Movies.Result>> = MutableLiveData()
    val top10NowPlayingMovies: LiveData<List<Movies.Result>>
        get() = _top10NowPlayingMovies

    private val _top10PopularMovies: MutableLiveData<List<Movies.Result>> = MutableLiveData()
    val top10PopularMovies: LiveData<List<Movies.Result>>
        get() = _top10PopularMovies

    private val _top10RatedMovies: MutableLiveData<List<Movies.Result>> = MutableLiveData()
    val top10RatedMovies: LiveData<List<Movies.Result>>
        get() = _top10RatedMovies

    private val _top10UpcomingMovies: MutableLiveData<List<Movies.Result>> = MutableLiveData()
    val top10UpcomingMovies: LiveData<List<Movies.Result>>
        get() = _top10UpcomingMovies

    private val genres: MutableList<Genre> = mutableListOf()

    fun update() {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            val nowPlayingFetchingJob = repository.fetchTop10NowPlayingMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    _top10NowPlayingMovies.postValue(movies)
                }
            }

            val popularFetchingJob = repository.fetchTop10PopularMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    _top10PopularMovies.postValue(movies)
                }
            }

            val topRatedFetchingJob = repository.fetchTop10HighRateMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    _top10RatedMovies.postValue(movies)
                }
            }

            val upcomingFetchingJob = repository.fetchTop10UpcomingMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    _top10UpcomingMovies.postValue(movies)
                }
            }

            val genresFetchingJob = repository.fetchGenres { genres ->
                if (genres != emptyList<Genre>()) {
                    this@MoviesViewModel.genres.apply {
                        clear()
                        addAll(genres)
                    }
                }
            }

            joinAll(
                nowPlayingFetchingJob,
                popularFetchingJob,
                topRatedFetchingJob,
                upcomingFetchingJob,
                genresFetchingJob
            )

            val randomCategory = (1..4).random()
            val randomRank = (1..10).random()

            val randomMovie = when (randomCategory) {
                NOW_PLAYING_MOVIES -> {
                    top10NowPlayingMovies.value?.let { movies ->
                        getRandomMovie(randomCategory, randomRank, movies)
                    }
                }
                POPULAR_MOVIES -> {
                    top10PopularMovies.value?.let { movies ->
                        getRandomMovie(randomCategory, randomRank, movies)
                    }
                }
                TOP_RATED_MOVIES -> {
                    top10RatedMovies.value?.let { movies ->
                        getRandomMovie(randomCategory, randomRank, movies)
                    }
                }
                UPCOMING_MOVIES -> {
                    top10UpcomingMovies.value?.let { movies ->
                        getRandomMovie(randomCategory, randomRank, movies)
                    }
                }
                else -> {
                    top10NowPlayingMovies.value?.let { movies ->
                        getRandomMovie(randomCategory, randomRank, movies)
                    }
                }
            }

            _recommendedMovie.postValue(randomMovie)

            _isLoading.postValue(false)
        }
    }

    private fun getRandomMovie(randomCatetory: Int, randomRank: Int, movies: List<Movies.Result>): RecommendedMovie {
        val index = if (randomRank < movies.size) {
            randomRank - 1
        } else {
            movies.lastIndex
        }

        val movie = movies[index]
        val reason = when (randomCatetory) {
            NOW_PLAYING_MOVIES -> "Now Playing"
            POPULAR_MOVIES -> "Popular"
            TOP_RATED_MOVIES -> "High Rated"
            UPCOMING_MOVIES -> "Upcoming"
            else -> ""
        }

        return RecommendedMovie(
            movie = movie,
            genres = genreIdsToGenreList(movie.genreIds, genres),
            recommendationReason = reason
        )
    }

    private fun genreIdsToGenreList(genreIds: List<Int>, genres: List<Genre>): List<Genre> {
        val result = mutableListOf<Genre>()
        genres.forEach { genre ->
            if (genreIds.contains(genre.id)) {
                result.add(genre)
            }
        }
        return result
    }
}

package io.woong.filmpedia.ui.page.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Genre
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.repository.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    companion object {
        private const val NOW_PLAYING_MOVIES: Int = 1
        private const val POPULAR_MOVIES: Int = 2
        private const val TOP_RATED_MOVIES: Int = 3
        private const val UPCOMING_MOVIES: Int = 4
    }

    private val repository: HomeRepository = HomeRepository()

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

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

    private val genres: MutableList<Genre> = mutableListOf()

    fun update() {
        CoroutineScope(Dispatchers.Default).launch {
            isLoading.postValue(true)

            val nowPlayingFetchingJob = repository.fetchTop10NowPlayingMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    top10NowPlayingMovies.postValue(movies)
                }
            }

            val popularFetchingJob = repository.fetchTop10PopularMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    top10PopularMovies.postValue(movies)
                }
            }

            val topRatedFetchingJob = repository.fetchTop10HighRateMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    top10RatedMovies.postValue(movies)
                }
            }

            val upcomingFetchingJob = repository.fetchTop10UpcomingMovies { movies ->
                if (movies != emptyList<Movies.Result>()) {
                    top10UpcomingMovies.postValue(movies)
                }
            }

            val genresFetchingJob = repository.fetchGenres { genres ->
                if (genres != emptyList<Genre>()) {
                    this@HomeViewModel.genres.apply {
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
                        getRandomMovie(randomRank, movies)
                    }
                }
                POPULAR_MOVIES -> {
                    top10PopularMovies.value?.let { movies ->
                        getRandomMovie(randomRank, movies)
                    }
                }
                TOP_RATED_MOVIES -> {
                    top10RatedMovies.value?.let { movies ->
                        getRandomMovie(randomRank, movies)
                    }
                }
                UPCOMING_MOVIES -> {
                    top10UpcomingMovies.value?.let { movies ->
                        getRandomMovie(randomRank, movies)
                    }
                }
                else -> {
                    top10NowPlayingMovies.value?.let { movies ->
                        getRandomMovie(randomRank, movies)
                    }
                }
            }

            recommendedMovie.postValue(randomMovie)

            isLoading.postValue(false)
        }
    }

    private fun getRandomMovie(randomRank: Int, movies: List<Movies.Result>): RecommendedMovie {
        val index = if (randomRank < movies.size) {
            randomRank - 1
        } else {
            movies.lastIndex
        }

        val movie = movies[index]

        return RecommendedMovie(
            movieId = movie.id,
            title = movie.title,
            backdropPath = movie.backdropPath,
            genres = genreIdsToGenreList(movie.genreIds, genres)
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

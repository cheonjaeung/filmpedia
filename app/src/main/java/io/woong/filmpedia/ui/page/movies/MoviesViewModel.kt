package io.woong.filmpedia.ui.page.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.data.movie.RecommendedMovie
import io.woong.filmpedia.repository.GenreRepository
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

    private val movieRepository: MovieRepository = MovieRepository()
    private val genreRepository: GenreRepository = GenreRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _recommendedMovie: MutableLiveData<RecommendedMovie> = MutableLiveData()
    val recommendedMovie: LiveData<RecommendedMovie>
        get() = _recommendedMovie

    private val _top10NowPlayingMovies: MutableLiveData<List<Movies.Movie>> = MutableLiveData()
    val top10NowPlayingMovies: LiveData<List<Movies.Movie>>
        get() = _top10NowPlayingMovies

    private val _top10PopularMovies: MutableLiveData<List<Movies.Movie>> = MutableLiveData()
    val top10PopularMovies: LiveData<List<Movies.Movie>>
        get() = _top10PopularMovies

    private val _top10RatedMovies: MutableLiveData<List<Movies.Movie>> = MutableLiveData()
    val top10RatedMovies: LiveData<List<Movies.Movie>>
        get() = _top10RatedMovies

    private val _top10UpcomingMovies: MutableLiveData<List<Movies.Movie>> = MutableLiveData()
    val top10UpcomingMovies: LiveData<List<Movies.Movie>>
        get() = _top10UpcomingMovies

    private val genres: MutableList<Genres.Genre> = mutableListOf()

    fun update(apiKey: String, language: String, region: String) {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            val nowPlayingFetchingJob = movieRepository.fetchTop10NowPlayingMovies(
                key = apiKey,
                page = 1,
                lang = language,
                region = region
            ) { movies ->
                if (movies != emptyList<Movies.Movie>()) {
                    _top10NowPlayingMovies.postValue(movies)
                }
            }

            val popularFetchingJob = movieRepository.fetchTop10PopularMovies(
                key = apiKey,
                page = 1,
                lang = language,
                region = region
            ) { movies ->
                if (movies != emptyList<Movies.Movie>()) {
                    _top10PopularMovies.postValue(movies)
                }
            }

            val topRatedFetchingJob = movieRepository.fetchTop10HighRateMovies(
                key = apiKey,
                page = 1,
                lang = language,
                region = region
            ) { movies ->
                if (movies != emptyList<Movies.Movie>()) {
                    _top10RatedMovies.postValue(movies)
                }
            }

            val upcomingFetchingJob = movieRepository.fetchTop10UpcomingMovies(
                key = apiKey,
                page = 1,
                lang = language,
                region = region
            ) { movies ->
                if (movies != emptyList<Movies.Movie>()) {
                    _top10UpcomingMovies.postValue(movies)
                }
            }

            val genresFetchingJob = genreRepository.fetchGenres(key = apiKey, lang = language) { genres ->
                if (genres != emptyList<Genres.Genre>()) {
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

    private fun getRandomMovie(randomCategory: Int, randomRank: Int, movies: List<Movies.Movie>): RecommendedMovie {
        val index = if (randomRank < movies.size) {
            randomRank - 1
        } else {
            movies.lastIndex
        }

        val movie = movies[index]
        val reason = when (randomCategory) {
            NOW_PLAYING_MOVIES -> R.string.now_playing
            POPULAR_MOVIES -> R.string.popular
            TOP_RATED_MOVIES -> R.string.high_rated
            UPCOMING_MOVIES -> R.string.upcoming
            else -> 0
        }

        return RecommendedMovie(
            movie = movie,
            genres = genreIdsToGenreList(movie.genreIds, genres),
            recommendationReason = reason
        )
    }

    private fun genreIdsToGenreList(genreIds: List<Int>, genres: List<Genres.Genre>): List<Genres.Genre> {
        val result = mutableListOf<Genres.Genre>()
        genres.forEach { genre ->
            if (genreIds.contains(genre.id)) {
                result.add(genre)
            }
        }
        return result
    }
}

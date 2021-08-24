package io.woong.filmpedia.ui.page.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.ExternalIds
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.repository.MovieRepository
import io.woong.filmpedia.util.isNotNullOrBlank
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

    private val _casts: MutableLiveData<List<Credits.Cast>> = MutableLiveData()
    val casts: LiveData<List<Credits.Cast>>
        get() = _casts

    private val _crews: MutableLiveData<List<Credits.Crew>> = MutableLiveData()
    val crews: LiveData<List<Credits.Crew>>
        get() = _crews

    private val crewsPriority: Map<String, Int> = mapOf(
        "Directing" to 1,
        "Production" to 2,
        "Writing" to 3,
        "Editing" to 4,
        "Camera" to 5,
        "Sound" to 6,
        "Lighting" to 7,
        "Visual Effects" to 8,
        "Art" to 9,
        "Costume & Make-Up" to 10,
        "Crew" to 11
    )

    private val _socialIds: MutableLiveData<ExternalIds> = MutableLiveData()
    val socialIds: LiveData<ExternalIds>
        get() = _socialIds

    private val _homepageEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val homepageEnabled: LiveData<Boolean>
        get() = _homepageEnabled

    private val _facebookEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val facebookEnabled: LiveData<Boolean>
        get() = _facebookEnabled

    private val _instagramEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val instagramEnabled: LiveData<Boolean>
        get() = _instagramEnabled

    private val _twitterEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val twitterEnabled: LiveData<Boolean>
        get() = _twitterEnabled

    private val _imdbEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val imdbEnabled: LiveData<Boolean>
        get() = _imdbEnabled

    private val _recommendationMovies: MutableLiveData<List<Movies.Movie>> = MutableLiveData()
    val recommendationMovies: LiveData<List<Movies.Movie>>
        get() = _recommendationMovies

    fun update(apiKey: String, movieId: Int, language: String) {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            val detailFetchingJob = repository.fetchMovieDetail(key = apiKey, id = movieId, lang = language) { movie ->
                movie?.let { m ->
                    _movie.postValue(m)

                    if (m.homepage.isNotNullOrBlank()) {
                        _homepageEnabled.postValue(true)
                    }
                }
            }

            val socialFetchingJob = repository.fetchExternalIds(key = apiKey, id = movieId) { ids ->
                ids?.let { i ->
                    _socialIds.postValue(i)

                    if (i.facebookId.isNotNullOrBlank()) {
                        _facebookEnabled.postValue(true)
                    }

                    if (i.instagramId.isNotNullOrBlank()) {
                        _instagramEnabled.postValue(true)
                    }

                    if (i.twitterId.isNotNullOrBlank()) {
                        _twitterEnabled.postValue(true)
                    }

                    if (i.imdbId.isNotNullOrBlank()) {
                        _imdbEnabled.postValue(true)
                    }
                }
            }

            val creditsFetchingJob = repository.fetchCredits(key = apiKey, id = movieId, lang = language) { credits ->
                credits?.let { c ->
                    _casts.postValue(c.cast)

                    val sorted = c.crew.sortedBy { crewsPriority[it.department] }
                    _crews.postValue(sorted)
                }
            }

            val recommendationFetchingJob = repository.fetchRecommendations(key = apiKey, id = movieId, lang = language) { movies ->
                movies?.let { m ->
                    val list = m.results
                    if (list.size < 5) {
                        _recommendationMovies.postValue(list)
                    } else {
                        _recommendationMovies.postValue(list.subList(0, 5))
                    }
                }
            }

            joinAll(
                detailFetchingJob,
                socialFetchingJob,
                creditsFetchingJob,
                recommendationFetchingJob
            )

            _isLoading.postValue(false)
        }
    }
}

package io.woong.filmpedia.ui.page.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.repository.MovieRepository
import io.woong.filmpedia.util.isNotNullOrBlank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovieViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

    private val _isOnline: MutableLiveData<Boolean> = MutableLiveData(true)
    val isOnline: LiveData<Boolean>
        get() = _isOnline

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var isDetailLoading: Boolean = false
    private var isImageLoading: Boolean = false
    private var isSocialLoading: Boolean = false
    private var isCreditLoading: Boolean = false

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String>
        get() = _title

    private val _originalTitle: MutableLiveData<String> = MutableLiveData()
    val originalTitle: LiveData<String>
        get() = _originalTitle

    private val _poster: MutableLiveData<String> = MutableLiveData()
    val poster: LiveData<String>
        get() = _poster

    private val _slides: MutableLiveData<List<String>> = MutableLiveData()
    val slides: LiveData<List<String>>
        get() = _slides

    private val _releaseDate: MutableLiveData<String> = MutableLiveData()
    val releaseDate: LiveData<String>
        get() = _releaseDate

    private val _runtime: MutableLiveData<Int> = MutableLiveData()
    val runtime: LiveData<Int>
        get() = _runtime

    private val _genres: MutableLiveData<List<Genres.Genre>> = MutableLiveData()
    val genres: LiveData<List<Genres.Genre>>
        get() = _genres

    private val _rating: MutableLiveData<Double> = MutableLiveData()
    val rating: LiveData<Double>
        get() = _rating

    private val _homepage: MutableLiveData<String> = MutableLiveData()
    val homepage: LiveData<String>
        get() = _homepage
    private val _isHomepageVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isHomepageVisible: LiveData<Boolean>
        get() = _isHomepageVisible

    private val _facebook: MutableLiveData<String> = MutableLiveData()
    val facebook: LiveData<String>
        get() = _facebook
    private val _isFacebookVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isFacebookVisible: LiveData<Boolean>
        get() = _isFacebookVisible

    private val _instagram: MutableLiveData<String> = MutableLiveData()
    val instagram: LiveData<String>
        get() = _instagram
    private val _isInstagramVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isInstagramVisible: LiveData<Boolean>
        get() = _isInstagramVisible

    private val _twitter: MutableLiveData<String> = MutableLiveData()
    val twitter: LiveData<String>
        get() = _twitter
    private val _isTwitterVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isTwitterVisible: LiveData<Boolean>
        get() = _isTwitterVisible

    private val _tagline: MutableLiveData<String> = MutableLiveData()
    val tagline: LiveData<String>
        get() = _tagline
    private val _isTaglineVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isTaglineVisible: LiveData<Boolean>
        get() = _isTaglineVisible

    private val _overview: MutableLiveData<String> = MutableLiveData()
    val overview: LiveData<String>
        get() = _overview
    private val _isOverviewVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isOverviewVisible: LiveData<Boolean>
        get() = _isOverviewVisible

    private val _directorAndCasting: MutableLiveData<List<PersonSummary>> = MutableLiveData()
    val directorAndCasting: LiveData<List<PersonSummary>>
        get() = _directorAndCasting

    private val _series: MutableLiveData<Movie.Collection> = MutableLiveData()
    val series: LiveData<Movie.Collection>
        get() = _series
    private val _isSeriesVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isSeriesVisible: LiveData<Boolean>
        get() = _isSeriesVisible

    private val _spokenLanguages: MutableLiveData<List<String>> = MutableLiveData()
    val spokenLanguages: LiveData<List<String>>
        get() = _spokenLanguages

    private val _budget: MutableLiveData<Long> = MutableLiveData()
    val budget: LiveData<Long>
        get() = _budget

    private val _revenue: MutableLiveData<Long> = MutableLiveData()
    val revenue: LiveData<Long>
        get() = _revenue

    fun load(apiKey: String, language: String, movieId: Int) = CoroutineScope(Dispatchers.Default).launch {
        _isOnline.postValue(true)
        _isLoading.postValue(true)
        isDetailLoading = true
        isImageLoading = true
        isSocialLoading = true
        isCreditLoading = true

        repository.fetchMovieDetail(key = apiKey, lang = language, id = movieId) { result ->
            result.onSuccess {
                _title.postValue(it.title)
                _originalTitle.postValue(it.originalTitle)
                _poster.postValue(it.posterPath)
                _releaseDate.postValue(it.releaseDate)
                _runtime.postValue(it.runtime)
                _rating.postValue(it.voteAverage)
                _genres.postValue(it.genres)

                _spokenLanguages.postValue(translateSpokenLanguages(language, it.spokenLanguages))
                _budget.postValue(it.budget)
                _revenue.postValue(it.revenue)

                _homepage.postValue(it.homepage)
                _isHomepageVisible.postValue(it.homepage.isNotNullOrBlank())

                _tagline.postValue(it.tagline)
                _isTaglineVisible.postValue(it.tagline.isNotNullOrBlank())
                _overview.postValue(it.overview)
                _isOverviewVisible.postValue(it.overview.isNotNullOrBlank())

                _series.postValue(it.belongsToCollection)
                _isSeriesVisible.postValue(it.belongsToCollection != null)
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isDetailLoading = false
            finishLoadingIfPossible()
        }

        repository.fetchImages(key = apiKey, id = movieId) { result ->
            result.onSuccess {
                val slides = it.backdrops
                if (slides.isNotEmpty()) {
                    val slidePaths = mutableListOf<String>()
                    slides.forEach { slide ->
                        slidePaths.add(slide.path)
                    }
                    _slides.postValue(slidePaths)
                }
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isImageLoading = false
            finishLoadingIfPossible()
        }

        repository.fetchExternalIds(key = apiKey, id = movieId) { result ->
            result.onSuccess {
                _facebook.postValue(it.facebookId)
                _instagram.postValue(it.instagramId)
                _twitter.postValue(it.twitterId)

                _isFacebookVisible.postValue(it.facebookId.isNotNullOrBlank())
                _isInstagramVisible.postValue(it.instagramId.isNotNullOrBlank())
                _isTwitterVisible.postValue(it.twitterId.isNotNullOrBlank())
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isSocialLoading = false
            finishLoadingIfPossible()
        }

        repository.fetchCredits(key = apiKey, lang = language, id = movieId) { result ->
            result.onSuccess {
                var count = 10
                val people = mutableListOf<PersonSummary>()

                val directors = extractDirectors(it.crew)
                count -= directors.size
                people.addAll(directors)

                people.addAll(subCastings(it.cast, count))

                _directorAndCasting.postValue(people)
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isCreditLoading = false
            finishLoadingIfPossible()
        }
    }

    private fun translateSpokenLanguages(languageCode: String, languageList: List<Movie.Country>): List<String> {
        try {
            val parsed = languageCode.split("-")
            val language = parsed[0]
            val locale = Locale(language)

            val translatedList = mutableListOf<String>()
            for (index in languageList.indices) {
                val languageItem = languageList[index]
                val itemLocal = Locale(languageItem.iso639_1)
                val translatedLanguageName = itemLocal.getDisplayLanguage(locale)
                translatedList.add(translatedLanguageName)
            }

            return translatedList
        } catch (e: IndexOutOfBoundsException) {
            return emptyList()
        }
    }

    private fun extractDirectors(crewList: List<Credits.Crew>): List<PersonSummary> {
        val directorDepartment = "Director"
        val directors = mutableListOf<PersonSummary>()

        crewList.forEach { crew ->
            if (crew.job == directorDepartment) {
                directors.add(
                    PersonSummary(
                        crew.id,
                        crew.name,
                        crew.job,
                        crew.profilePath,
                    )
                )
            }
        }

        return directors
    }

    private fun subCastings(castList: List<Credits.Cast>, endIndex: Int): List<PersonSummary> {
        val casting = mutableListOf<PersonSummary>()

        for (index in 0 until endIndex) {
            if (index >= castList.lastIndex) {
                break
            }

            val cast = castList[index]
            casting.add(
                PersonSummary(
                    cast.id,
                    cast.name,
                    cast.character,
                    cast.profilePath
                )
            )
        }

        return casting
    }

    private fun finishLoadingIfPossible() {
        if (!isDetailLoading && !isImageLoading && !isSocialLoading && !isCreditLoading) {
            _isLoading.postValue(false)
        }
    }
}

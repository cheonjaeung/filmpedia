package io.woong.filmpedia.ui.page.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.lang.StringBuilder
import java.util.*

class MovieViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

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

    private val _runtime: MutableLiveData<String> = MutableLiveData()
    val runtime: LiveData<String>
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

    private val _facebook: MutableLiveData<String> = MutableLiveData()
    val facebook: LiveData<String>
        get() = _facebook

    private val _instagram: MutableLiveData<String> = MutableLiveData()
    val instagram: LiveData<String>
        get() = _instagram

    private val _twitter: MutableLiveData<String> = MutableLiveData()
    val twitter: LiveData<String>
        get() = _twitter

    private val _tagline: MutableLiveData<String> = MutableLiveData()
    val tagline: LiveData<String>
        get() = _tagline

    private val _overview: MutableLiveData<String> = MutableLiveData()
    val overview: LiveData<String>
        get() = _overview

    private val _directorAndCasting: MutableLiveData<List<PersonSummary>> = MutableLiveData()
    val directorAndCasting: LiveData<List<PersonSummary>>
        get() = _directorAndCasting

    private val _series: MutableLiveData<Movie.Collection> = MutableLiveData()
    val series: LiveData<Movie.Collection>
        get() = _series

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
        repository.fetchMovieDetail(key = apiKey, lang = language, id = movieId) { movie ->
            if (movie != null) {
                _title.postValue(movie.title)
                _originalTitle.postValue(movie.originalTitle)
                _poster.postValue(movie.posterPath)
                _releaseDate.postValue(movie.releaseDate)
                _runtime.postValue(convertRuntimeToString(movie.runtime))
                _rating.postValue(movie.voteAverage)
                _tagline.postValue(movie.tagline)
                _overview.postValue(movie.overview)
                _genres.postValue(movie.genres)
                _homepage.postValue(movie.homepage)
                _series.postValue(movie.belongsToCollection)
                _spokenLanguages.postValue(translateSpokenLanguages(language, movie.spokenLanguages))
                _budget.postValue(movie.budget)
                _revenue.postValue(movie.revenue)
            }
        }

        repository.fetchImages(key = apiKey, id = movieId) { images ->
            if (images != null) {
                val slides = images.backdrops
                if (slides.isNotEmpty()) {
                    val slidePaths = mutableListOf<String>()
                    slides.forEach { slide ->
                        slidePaths.add(slide.path)
                    }
                    _slides.postValue(slidePaths)
                }
            }
        }

        repository.fetchExternalIds(key = apiKey, id = movieId) { ids ->
            if (ids != null) {
                _facebook.postValue(ids.facebookId)
                _instagram.postValue(ids.instagramId)
                _twitter.postValue(ids.twitterId)
            }
        }

        repository.fetchCredits(key = apiKey, lang = language, id = movieId) { credits ->
            if (credits != null) {
                var count = 10
                val people = mutableListOf<PersonSummary>()

                val directors = extractDirectors(credits.crew)
                count -= directors.size
                people.addAll(directors)

                people.addAll(subCastings(credits.cast, count))

                _directorAndCasting.postValue(people)
            }
        }
    }

    private fun convertRuntimeToString(runtime: Int?): String? {
        return if (runtime != null) {
            val builder = StringBuilder()
            var time = runtime

            val hours = time / 60
            time %= 60
            builder.append("(${hours}h ${time}m)")

            builder.toString()
        } else {
            null
        }
    }

    private fun translateSpokenLanguages(languageCode: String, list: List<Movie.Country>): List<String> {
        try {
            val parsed = languageCode.split("-")
            val language = parsed[0]
            val country = parsed[1]
            val locale = Locale(language, country)

            val translatedList = mutableListOf<String>()
            list.forEach {
                val translated = locale.getDisplayLanguage(Locale(it.iso3166_1))
                translatedList.add(translated)
            }

            return translatedList
        } catch (e: IndexOutOfBoundsException) {
            return emptyList()
        } catch (e: NullPointerException) {
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
}

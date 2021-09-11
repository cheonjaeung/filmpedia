package io.woong.filmpedia.ui.page.person

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.Filmography
import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.data.people.Person
import io.woong.filmpedia.repository.PeopleRepository
import io.woong.filmpedia.util.isNotNullOrBlank
import kotlinx.coroutines.*

class PersonViewModel : ViewModel() {

    private val repository: PeopleRepository = PeopleRepository()

    private val _isOnline: MutableLiveData<Boolean> = MutableLiveData(true)
    val isOnline: LiveData<Boolean>
        get() = _isOnline

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private var isDetailLoading: Boolean = false
    private var isBiographyLoading: Boolean = false
    private var isCreditLoading: Boolean = false

    private var profile: Filmography? = null
    private var biography: Filmography? = null
    private var movies: MutableList<Filmography> = mutableListOf()

    private val _filmography: MutableLiveData<List<Filmography>> = MutableLiveData()
    val filmography: LiveData<List<Filmography>>
        get() = _filmography

    fun load(
        context: Context,
        personId: Int,
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        _isLoading.postValue(true)

        loadDetail(personId, apiKey, language)
        loadBiography(personId, apiKey, language, region)
        loadCredits(context, personId, apiKey, language)
    }

    private fun loadDetail(
        personId: Int,
        apiKey: String,
        language: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        isDetailLoading = true

        repository.fetchDetail(key = apiKey, id = personId, lang = language) { result ->
            result.onSuccess {
                val gender = when (it.gender) {
                    Person.Gender.MALE.value -> Person.Gender.MALE
                    Person.Gender.FEMALE.value -> Person.Gender.FEMALE
                    Person.Gender.NON_BINARY.value -> Person.Gender.NON_BINARY
                    else -> Person.Gender.UNSPECIFIED
                }

                profile = Filmography.Profile(
                    it.name,
                    it.profilePath,
                    it.birthday,
                    it.deathday,
                    gender,
                    it.placeOfBirth
                )
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isDetailLoading = false
            mergeData()
        }
    }

    private fun loadBiography(
        personId: Int,
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        isBiographyLoading = true

        repository.fetchTranslations(key = apiKey, id = personId, lang = language) { result ->
            result.onSuccess {
                val list = it.translations

                val biography = if (list.isNotEmpty()) {
                    var index = 0
                    var englishIndex = 0
                    for (item in list) {
                        if (item.iso3166_1 == "US" && item.iso639_1 == "en") {
                            englishIndex = index
                        }
                        if (item.iso3166_1 == region) {
                            break
                        }
                        index++
                    }

                    val bio = if (index < list.lastIndex) {
                        val bioTemp = list[index].translated.biography

                        if (bioTemp.isNotNullOrBlank()) {
                            bioTemp
                        } else {
                            list[englishIndex].translated.biography
                        }
                    } else {
                        list[englishIndex].translated.biography
                    }

                    if (bio.isNotBlank()) {
                        Filmography.Biography(bio)
                    } else {
                        Filmography.Biography(null)
                    }
                } else {
                    Filmography.Biography(null)
                }

                this@PersonViewModel.biography = biography
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isBiographyLoading = false
            mergeData()
        }
    }

    private fun loadCredits(
        context: Context,
        personId: Int,
        apiKey: String,
        language: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        isCreditLoading = true

        repository.fetchMovieCredits(key = apiKey, id = personId, lang = language) { result ->
            result.onSuccess { credits ->
                val castList = credits.casts
                val crewList = credits.crews

                val pair = crewList.divideDirectorAndStaff()

                val list = mutableListOf<Filmography>()

                val sortedCastList = castList.sortedByDescending { it.releaseDate }
                val actingMovies = sortedCastList.toFilmographyList()
                if (actingMovies.isNotEmpty()) {
                    list.add(createTitleItem(context.getString(R.string.person_filmography_acting)))
                    list.addAll(actingMovies)
                }

                val directingList = pair.first
                val sortedDirectingList = directingList.sortedByDescending { it.releaseDate }
                val directingMovies = sortedDirectingList.toFilmographyList()
                if (directingList.isNotEmpty()) {
                    list.add(createTitleItem(context.getString(R.string.person_filmography_directing)))
                    list.addAll(directingMovies)
                }

                val staffList = pair.second
                val sortedStaffList = staffList.sortedByDescending { it.releaseDate }
                val staffMovies = sortedStaffList.toFilmographyList()
                if (staffMovies.isNotEmpty()) {
                    list.add(createTitleItem(context.getString(R.string.person_filmography_staff)))
                    list.addAll(staffMovies)
                }

                movies.addAll(list)
            }.onNetworkError {
                _isOnline.postValue(false)
            }

            isCreditLoading = false
            mergeData()
        }
    }

    private fun mergeData() {
        if (!isDetailLoading && !isBiographyLoading && !isCreditLoading) {
            val list = mutableListOf<Filmography>()
            profile?.let { list.add(it) }
            biography?.let { list.add(it) }
            movies.forEach {
                list.add(it)
            }
            _filmography.postValue(list)

            _isLoading.postValue(false)
        }
    }

    private fun List<MovieCredits.SubItem>.toFilmographyList(): List<Filmography> {
        val filmographies = mutableListOf<Filmography>()

        var currentYear = ""
        for ((index, item) in this.withIndex()) {
            if (index == 0) {
                val new = item.toFilmography()
                filmographies.add(new)
                currentYear = (new as Filmography.Movie).releaseYear
            } else {
                val new = item.toFilmography()
                val releaseYear = (new as Filmography.Movie).releaseYear

                if (releaseYear == currentYear) {
                    filmographies.add(new)
                } else {
                    currentYear = releaseYear
                    filmographies.add(createDividerItem())
                    filmographies.add(new)
                }
            }
        }

        return filmographies
    }

    private fun List<MovieCredits.Crew>.divideDirectorAndStaff(): Pair<List<MovieCredits.Crew>, List<MovieCredits.Crew>> {
        val directingList = mutableListOf<MovieCredits.Crew>()
        val staffList = mutableListOf<MovieCredits.Crew>()

        for (item in this) {
            val job = item.job
            if (job == "Director") {
                directingList.add(item)
            } else {
                staffList.add(item)
            }
        }

        return directingList to staffList
    }

    private fun MovieCredits.SubItem.toFilmography(): Filmography {
        val year = if (releaseDate.isNotNullOrBlank()) {
            releaseDate!!.substring(0, 4)
        } else {
            "-"
        }

        val department = if (this is MovieCredits.Cast) {
            if (character.isNotBlank()) {
                character
            } else {
                "-"
            }
        } else {
            this as MovieCredits.Crew
            if (job.isNotBlank()) {
                job
            } else {
                "-"
            }
        }

        return Filmography.Movie(id, title, year, department)
    }

    private fun createTitleItem(title: String): Filmography = Filmography.Title(title)

    private fun createDividerItem(): Filmography = Filmography.Divider()
}

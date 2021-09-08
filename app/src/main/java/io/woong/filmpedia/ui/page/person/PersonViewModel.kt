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

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var profile: Filmography.Profile? = null
    private var biography: Filmography.Biography? = null
    private var actingMovies: MutableList<Filmography> = mutableListOf()
    private var directingMovies: MutableList<Filmography> = mutableListOf()
    private var staffMovies: MutableList<Filmography> = mutableListOf()

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

        val detailJob = loadDetail(personId, apiKey, language)
        val biographyJob = loadBiography(personId, apiKey, language, region)
        val creditJob = loadCredits(personId, apiKey, language)

        joinAll(detailJob, biographyJob, creditJob)

        val list = mutableListOf<Filmography>()
        profile?.let {
            list.add(it)
        }
        biography?.let {
            list.add(it)
        }
        if (actingMovies.isNotEmpty()) {
            list.add(createTitleItem(context.getString(R.string.person_filmography_acting)))
            list.addAll(actingMovies)
        }
        if (directingMovies.isNotEmpty()) {
            list.add(createTitleItem(context.getString(R.string.person_filmography_directing)))
            list.addAll(directingMovies)
        }
        if (staffMovies.isNotEmpty()) {
            list.add(createTitleItem(context.getString(R.string.person_filmography_staff)))
            list.addAll(staffMovies)
        }

        _filmography.postValue(list)

        _isLoading.postValue(false)
    }

    private fun loadDetail(personId: Int, apiKey: String, language: String) = CoroutineScope(Dispatchers.Default).launch {
        repository.fetchDetail(key = apiKey, id = personId, lang = language) { person ->
            if (person != null) {
                val gender = when (person.gender) {
                    Person.Gender.MALE.value -> Person.Gender.MALE
                    Person.Gender.FEMALE.value -> Person.Gender.FEMALE
                    Person.Gender.NON_BINARY.value -> Person.Gender.NON_BINARY
                    else -> Person.Gender.UNSPECIFIED
                }

                profile = Filmography.Profile(
                    person.name,
                    person.profilePath,
                    person.birthday,
                    person.deathday,
                    gender,
                    person.placeOfBirth
                )
            }
        }.join()
    }

    private fun loadBiography(
        personId: Int,
        apiKey: String,
        language: String,
        region: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        repository.fetchTranslations(
            key = apiKey,
            id = personId,
            lang = language
        ) { translations ->
            if (translations != null) {
                val list = translations.translations

                if (list.isNotEmpty()) {
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

                    biography = if (bio.isNotBlank()) {
                        Filmography.Biography(bio)
                    } else {
                        Filmography.Biography(null)
                    }
                } else {
                    biography = Filmography.Biography(null)
                }
            } else {
                biography = Filmography.Biography(null)
            }
        }.join()
    }

    private fun loadCredits(personId: Int, apiKey: String, language: String) = CoroutineScope(Dispatchers.Default).launch {
        repository.fetchMovieCredits(key = apiKey, id = personId, lang = language) { credits ->
            if (credits != null) {
                val castList = credits.casts
                val crewList = credits.crews

                val pair = crewList.divideDirectorAndStaff()

                val sortedCastList = castList.sortedByDescending { it.releaseDate }
                actingMovies.addAll(sortedCastList.toFilmographyList())

                val directingList = pair.first
                val sortedDirectingList = directingList.sortedByDescending { it.releaseDate }
                directingMovies.addAll(sortedDirectingList.toFilmographyList())

                val staffList = pair.second
                val sortedStaffList = staffList.sortedByDescending { it.releaseDate }
                staffMovies.addAll(sortedStaffList.toFilmographyList())
            }
        }.join()
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

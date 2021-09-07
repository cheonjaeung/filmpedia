package io.woong.filmpedia.ui.page.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.people.Filmography
import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.data.people.Person
import io.woong.filmpedia.repository.PeopleRepository
import io.woong.filmpedia.util.isNotNullOrBlank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class PersonViewModel : ViewModel() {

    private val repository: PeopleRepository = PeopleRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _profile: MutableLiveData<String> = MutableLiveData()
    val profile: LiveData<String>
        get() = _profile

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String>
        get() = _name

    private val _birthday: MutableLiveData<String> = MutableLiveData()
    val birthday: LiveData<String>
        get() = _birthday

    private val _deathday: MutableLiveData<String> = MutableLiveData()
    val deathday: LiveData<String>
        get() = _deathday

    private val _gender: MutableLiveData<Person.Gender> = MutableLiveData()
    val gender: LiveData<Person.Gender>
        get() = _gender

    private val _placeOfBirth: MutableLiveData<String> = MutableLiveData()
    val placeOfBirth: LiveData<String>
        get() = _placeOfBirth
    private val _isPlaceOfBirthVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isPlaceOfBirthVisible: LiveData<Boolean>
        get() = _isPlaceOfBirthVisible

    private val _biography: MutableLiveData<String> = MutableLiveData()
    val biography: LiveData<String>
        get() = _biography
    private val _isBiographyVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    val isBiographyVisible: LiveData<Boolean>
        get() = _isBiographyVisible

    private val _actingMovies: MutableLiveData<List<Filmography>> = MutableLiveData()
    val actingMovies: LiveData<List<Filmography>>
        get() = _actingMovies
    private val _isActingMoviesVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isActingMoviesVisible: LiveData<Boolean>
        get() = _isActingMoviesVisible

    private val _directingMovies: MutableLiveData<List<Filmography>> = MutableLiveData()
    val directingMovies: LiveData<List<Filmography>>
        get() = _directingMovies
    private val _isDirectingMoviesVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDirectingMoviesVisible: LiveData<Boolean>
        get() = _isDirectingMoviesVisible

    private val _staffMovies: MutableLiveData<List<Filmography>> = MutableLiveData()
    val staffMovies: LiveData<List<Filmography>>
        get() = _staffMovies
    private val _isStaffMoviesVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isStaffMoviesVisible: LiveData<Boolean>
        get() = _isStaffMoviesVisible

    fun load(personId: Int, apiKey: String, language: String, region: String) {
        _isLoading.postValue(true)

        CoroutineScope(Dispatchers.Default).launch {
            val detailJob = repository.fetchDetail(key = apiKey, id = personId, lang = language) { person ->
                if (person != null) {
                    _profile.postValue(person.profilePath)
                    _name.postValue(person.name)
                    _birthday.postValue(person.birthday)
                    _deathday.postValue(person.deathday)

                    _gender.postValue(
                        when (person.gender) {
                            Person.Gender.MALE.value -> Person.Gender.MALE
                            Person.Gender.FEMALE.value -> Person.Gender.FEMALE
                            Person.Gender.NON_BINARY.value -> Person.Gender.NON_BINARY
                            else -> Person.Gender.UNSPECIFIED
                        }
                    )

                    val pob = person.placeOfBirth
                    if (pob != null) {
                        _placeOfBirth.postValue(pob)
                        _isPlaceOfBirthVisible.postValue(true)
                    } else {
                        _isPlaceOfBirthVisible.postValue(false)
                    }
                }
            }

            val biographyJob = repository.fetchTranslations(key = apiKey, id = personId, lang = language) { translations ->
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

                        if (bio.isNotBlank()) {
                            _biography.postValue(bio)
                            _isBiographyVisible.postValue(true)
                        } else {
                            _isBiographyVisible.postValue(false)
                        }
                    } else {
                        _isBiographyVisible.postValue(false)
                    }
                } else {
                    _isBiographyVisible.postValue(false)
                }
            }

            val creditsJob = repository.fetchMovieCredits(key = apiKey, id = personId, lang = language) { credits ->
                if (credits != null) {
                    val castList = credits.casts
                    val crewList = credits.crews

                    CoroutineScope(Dispatchers.Default).launch {
                        val sortedCastList = castList.sortedByDescending { it.releaseDate }
                        val actingList = sortedCastList.toFilmographyList()
                        _actingMovies.postValue(actingList)
                        _isActingMoviesVisible.postValue(actingList.isNotEmpty())
                    }

                    val pair = crewList.divideDirectorAndStaff()

                    CoroutineScope(Dispatchers.Default).launch {
                        val extractedList = pair.first
                        val sortedList = extractedList.sortedByDescending { it.releaseDate }
                        val directingList = sortedList.toFilmographyList()
                        _directingMovies.postValue(directingList)
                        _isDirectingMoviesVisible.postValue(directingList.isNotEmpty())
                    }

                    CoroutineScope(Dispatchers.Default).launch {
                        val extractedList = pair.second
                        val sortedList = extractedList.sortedByDescending { it.releaseDate }
                        val staffList = sortedList.toFilmographyList()
                        _staffMovies.postValue(staffList)
                        _isStaffMoviesVisible.postValue(staffList.isNotEmpty())
                    }
                }
            }

            joinAll(detailJob, biographyJob, creditsJob)

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
                currentYear = new.releasedYear
            } else {
                val new = item.toFilmography()

                if (new.releasedYear == currentYear) {
                    filmographies.add(new)
                } else {
                    currentYear = new.releasedYear
                    filmographies.add(createDividerFilmography())
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

        return Filmography(
            id,
            FilmographyListAdapter.ITEM_VIEW_TYPE,
            year,
            title,
            department
        )
    }

    private fun createDividerFilmography(): Filmography = Filmography(
        0,
        FilmographyListAdapter.DIVIDER_VIEW_TYPE,
        "",
        "",
        ""
    )
}

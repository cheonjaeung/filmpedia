package io.woong.filmpedia.ui.page.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _castedMovies: MutableLiveData<List<MovieCredits.Cast>> = MutableLiveData()
    val castedMovies: LiveData<List<MovieCredits.Cast>>
        get() = _castedMovies

    private val _producedMovies: MutableLiveData<List<MovieCredits.Crew>> = MutableLiveData()
    val producedMovies: LiveData<List<MovieCredits.Crew>>
        get() = _producedMovies

    fun update(personId: Int, apiKey: String, language: String, region: String) {
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
                    _castedMovies.postValue(credits.casts)
                    _producedMovies.postValue(credits.crews)
                }
            }

            joinAll(detailJob, biographyJob, creditsJob)
        }
    }
}

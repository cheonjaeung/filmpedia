package io.woong.filmpedia.ui.page.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.repository.PeopleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class PersonViewModel : ViewModel() {

    private val repository: PeopleRepository = PeopleRepository()

    private val _profile: MutableLiveData<String> = MutableLiveData()
    val profile: LiveData<String>
        get() = _profile

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String>
        get() = _name

    private val _biography: MutableLiveData<String> = MutableLiveData()
    val biography: LiveData<String>
        get() = _biography

    private val _birthdayAndDeathday: MutableLiveData<String> = MutableLiveData()
    val birthdayAndDeathday: LiveData<String>
        get() = _birthdayAndDeathday

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

                    val birthday = person.birthday
                    val deathday = person.deathday

                    birthday?.let {
                        val builder = StringBuilder(birthday)

                        if (person.deathday != null) {
                            builder.append(" - ")
                            builder.append(deathday)
                        }

                        _birthdayAndDeathday.postValue(builder.toString())
                    }
                }
            }

            val biographyJob = repository.fetchTranslations(key = apiKey, id = personId, lang = language) { translations ->
                if (translations != null) {
                    val list = translations.translations

                    var index = 0
                    for (item in list) {
                        if (item.iso3166_1 == region) {
                            break
                        }
                        index++
                    }

                    if (index < list.lastIndex) {
                        _biography.postValue(list[index].translated.biography)
                    } else {
                        _biography.postValue(list[0].translated.biography)
                    }
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

package io.woong.filmpedia.ui.page.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PeopleViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()

    private val _people: MutableLiveData<List<PersonSummary>> = MutableLiveData()
    val people: LiveData<List<PersonSummary>>
        get() = _people

    fun load(apiKey: String, language: String, movieId: Int) = CoroutineScope(Dispatchers.Default).launch {
        repository.fetchCredits(key = apiKey, lang = language, id = movieId) { credits ->
            if (credits != null) {
                val castList = credits.cast
                val crewList = credits.crew

                val list = mutableListOf<PersonSummary>()

                castList.forEach {
                    val item = PersonSummary(it.id, it.name, it.character, it.profilePath)
                    list.add(item)
                }

                crewList.forEach {
                    val item = PersonSummary(it.id, it.name, it.job, it.profilePath)
                    list.add(item)
                }

                _people.postValue(list)
            }
        }
    }
}

package io.woong.filmpedia.ui.page.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Person
import io.woong.filmpedia.repository.PeopleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonViewModel : ViewModel() {

    private val repository: PeopleRepository = PeopleRepository()

    private val _person: MutableLiveData<Person> = MutableLiveData()
    val person: LiveData<Person>
        get() = _person

    fun update(personId: Int, apiKey: String, language: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.fetchDetail(key = apiKey, id = personId, lang = language) { person ->
                if (person != null) {
                    _person.postValue(person)
                }
            }
        }
    }
}

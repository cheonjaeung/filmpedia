package io.woong.filmpedia.ui.page.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Collection
import io.woong.filmpedia.repository.CollectionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SeriesViewModel : ViewModel() {

    private val repository: CollectionRepository = CollectionRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _collection: MutableLiveData<Collection> = MutableLiveData()
    val collection: LiveData<Collection>
        get() = _collection

    fun update(collectionId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            repository.fetchDetail(collectionId = collectionId) { collection ->
                collection?.let { c ->
                    _collection.postValue(c)
                }
            }.join()

            _isLoading.postValue(false)
        }
    }
}

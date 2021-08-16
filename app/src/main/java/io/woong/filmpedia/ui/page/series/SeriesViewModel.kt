package io.woong.filmpedia.ui.page.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.Collection
import io.woong.filmpedia.repository.CollectionRepository
import io.woong.filmpedia.util.isNotNullOrBlank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SeriesViewModel : ViewModel() {

    private val repository: CollectionRepository = CollectionRepository()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _backdrop: MutableLiveData<String> = MutableLiveData()
    val backdrop: LiveData<String>
        get() = _backdrop

    private val _poster: MutableLiveData<String> = MutableLiveData()
    val poster: LiveData<String>
        get() = _poster

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String>
        get() = _title

    private val _overview: MutableLiveData<String> = MutableLiveData()
    val overview: LiveData<String>
        get() = _overview

    private val _movies: MutableLiveData<List<Collection.Part>> = MutableLiveData()
    val movies: LiveData<List<Collection.Part>>
        get() = _movies

    fun update(collectionId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            _isLoading.postValue(true)

            repository.fetchDetail(collectionId = collectionId) { collection ->
                collection?.let { c ->
                    _backdrop.postValue(c.backdropPath)
                    _poster.postValue(c.posterPath)
                    _title.postValue(c.name)
                    _overview.postValue(c.overview)

                    val sorted = c.parts.sortedBy { getPriorityByReleaseDate(it.releaseDate) }
                    _movies.postValue(sorted)
                }
            }.join()

            _isLoading.postValue(false)
        }
    }

    private fun getPriorityByReleaseDate(releaseDate: String?): Int {
        return if (releaseDate.isNotNullOrBlank()) {
            try {
                val split = releaseDate?.split("-")!!
                var num = 0
                num += split[2].toInt()
                num += split[1].toInt() * 100
                num += split[0].toInt() * 10_000
                num
            } catch (e: Throwable) {
                Int.MAX_VALUE
            }
        } else {
            Int.MAX_VALUE
        }
    }
}

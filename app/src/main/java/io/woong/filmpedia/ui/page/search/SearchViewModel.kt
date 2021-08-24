package io.woong.filmpedia.ui.page.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository: SearchRepository = SearchRepository()

    private val _results: MutableLiveData<List<Movies.Movie>> = MutableLiveData()
    val results: LiveData<List<Movies.Movie>>
        get() = _results

    private val _page: MutableLiveData<Int> = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    fun update(apiKey: String, language: String, region: String, query: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.fetchMovies(
                key = apiKey,
                lang = language,
                region = region,
                query = query,
                page = 1
            ) { searchResults ->
                searchResults?.let {
                    if (it.results.isNotEmpty()) {
                        _results.postValue(it.results)
                    }
                }
            }
        }
    }
}

package io.woong.filmpedia.ui.page.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.data.search.SearchResult
import io.woong.filmpedia.repository.GenreRepository
import io.woong.filmpedia.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val searchRepository: SearchRepository = SearchRepository()
    private val genreRepository: GenreRepository = GenreRepository()

    private var resultPage: Int = 1
    private var isResultLoading: Boolean = false
    private val _results: MutableLiveData<MutableList<SearchResult>> = MutableLiveData()
    val results: LiveData<MutableList<SearchResult>>
        get() = _results

    private val genreMap: HashMap<Int, String> = hashMapOf()
    private var _isGenresReady: Boolean = false
    private val isGenresReady: Boolean
        get() = _isGenresReady

    fun updateGenres(apiKey: String, language: String) = CoroutineScope(Dispatchers.Default).launch {
        genreRepository.fetchGenres(key = apiKey, lang = language) { genres ->
            if (genres.isNotEmpty()) {
                genres.forEach { genre ->
                    genreMap[genre.id] = genre.name
                }
                _isGenresReady = true
            }
        }
    }

    fun search(
        apiKey: String,
        language: String,
        region: String,
        query: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (!isResultLoading && isGenresReady) {
            isResultLoading = true

            searchRepository.fetchMovies(
                key = apiKey,
                lang = language,
                region = region,
                query = query,
                page = 1
            ) { searchResult ->
                if (searchResult != null) {
                    val list = searchResult.results
                    if (list.isNotEmpty()) {
                        val results = mutableListOf<SearchResult>()
                        list.forEach { movie ->
                            results.add(
                                SearchResult(
                                    movie.id,
                                    movie.title,
                                    movie.posterPath,
                                    convertGenreIdsToList(movie.genreIds),
                                    movie.voteAverage
                                )
                            )
                        }
                        _results.postValue(results)
                        resultPage = 1
                    }
                }
            }.join()

            isResultLoading = false
        }
    }

    fun searchNext(
        apiKey: String,
        language: String,
        region: String,
        query: String
    ) = CoroutineScope(Dispatchers.Default).launch {
        if (!isResultLoading && isGenresReady) {
            isResultLoading = true
            val nextPage = resultPage + 1

            searchRepository.fetchMovies(
                key = apiKey,
                lang = language,
                region = region,
                query = query,
                page = nextPage
            ) { searchResult ->
                if (searchResult != null) {
                    val list = searchResult.results
                    if (list.isNotEmpty()) {
                        val currentList = results.value ?: mutableListOf()
                        list.forEach { movie ->
                            currentList.add(
                                SearchResult(
                                    movie.id,
                                    movie.title,
                                    movie.posterPath,
                                    convertGenreIdsToList(movie.genreIds),
                                    movie.voteAverage
                                )
                            )
                        }
                        _results.postValue(currentList)
                        resultPage = nextPage
                    }
                }
            }

            isResultLoading = false
        }
    }

    private fun convertGenreIdsToList(ids: List<Int>): List<Genres.Genre> {
        val genres = mutableListOf<Genres.Genre>()
        ids.forEach { id ->
            val genreName = genreMap[id]
            if (genreName != null) {
                genres.add(Genres.Genre(id, genreName))
            }
        }
        return genres
    }
}

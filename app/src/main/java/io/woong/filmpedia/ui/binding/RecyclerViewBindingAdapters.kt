package io.woong.filmpedia.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.data.collection.Collection
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.data.people.Filmography
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.data.search.SearchResult
import io.woong.filmpedia.ui.page.home.MovieListAdapter
import io.woong.filmpedia.ui.page.person.FilmographyListAdapter
import io.woong.filmpedia.ui.page.search.SearchResultListAdapter
import io.woong.filmpedia.ui.page.series.SeriesMovieListAdapter

object RecyclerViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("list_item")
    fun RecyclerView.bindListItem(list: List<Any>?) {
        if (list != null) {
            when (this.adapter) {
                is MovieListAdapter -> {
                    val adapter = this.adapter as MovieListAdapter
                    adapter.items = list as List<Movies.Movie>
                }

                is io.woong.filmpedia.ui.page.movie.PeopleListAdapter -> {
                    val adapter = this.adapter as io.woong.filmpedia.ui.page.movie.PeopleListAdapter
                    adapter.people = list as List<PersonSummary>
                }

                is io.woong.filmpedia.ui.page.people.PeopleListAdapter -> {
                    val adapter = this.adapter as io.woong.filmpedia.ui.page.people.PeopleListAdapter
                    adapter.people = list as List<PersonSummary>
                }

                is FilmographyListAdapter -> {
                    val adapter = this.adapter as FilmographyListAdapter
                    adapter.items = list as List<Filmography>
                }

                is SearchResultListAdapter -> {
                    val adapter = this.adapter as SearchResultListAdapter
                    adapter.results = list as List<SearchResult>
                }

                is SeriesMovieListAdapter -> {
                    val adapter = this.adapter as SeriesMovieListAdapter
                    adapter.movies = list as List<Collection.Part>
                }
            }
        }
    }
}

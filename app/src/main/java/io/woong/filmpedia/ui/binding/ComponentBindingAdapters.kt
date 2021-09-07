package io.woong.filmpedia.ui.binding

import androidx.databinding.BindingAdapter
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.ui.component.GenresTextView
import io.woong.filmpedia.ui.component.SlideShowView

object ComponentBindingAdapters {

    @JvmStatic
    @BindingAdapter("genres")
    fun GenresTextView.bindGenres(genres: List<Genres.Genre>?) {
        if (genres != null) {
            this.genres = genres
        }
    }

    @JvmStatic
    @BindingAdapter("slides")
    fun SlideShowView.bindSlides(slides: List<String>?) {
        if (slides != null) {
            this.slides = slides
        }
    }
}

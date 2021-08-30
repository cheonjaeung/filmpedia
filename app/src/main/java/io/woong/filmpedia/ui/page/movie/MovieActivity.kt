package io.woong.filmpedia.ui.page.movie

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.databinding.ActivityMovieBinding
import io.woong.filmpedia.ui.component.GenresTextView
import io.woong.filmpedia.util.UriUtil
import io.woong.filmpedia.util.isNotNullOrBlank

class MovieActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID_EXTRA_ID: String = "movie_id"
        const val MOVIE_TITLE_EXTRA_ID: String = "movie_title"

        @JvmStatic
        @BindingAdapter("slides")
        fun ViewPager2.bindSlides(slidePaths: List<String>?) {
            if (slidePaths != null) {
                val adapter = this.adapter as SlideShowAdapter
                adapter.imagePaths = slidePaths
            }
        }

        @JvmStatic
        @BindingAdapter("poster")
        fun AppCompatImageView.bindPoster(poster: String?) {
            Glide.with(this)
                .load(UriUtil.getImageUrl(poster))
                .placeholder(R.drawable.placeholder_poster)
                .into(this)
        }

        @JvmStatic
        @BindingAdapter("genre_list")
        fun GenresTextView.bindGenres(genres: List<Genres.Genre>?) {
            if (genres != null) {
                this.visibility = View.VISIBLE
                this.genres = genres
            } else {
                this.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("tagline")
        fun AppCompatTextView.bindTagline(tagline: String?) {
            if (tagline.isNotNullOrBlank()) {
                this.visibility = View.VISIBLE
                this.text = tagline
            } else {
                this.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("overview")
        fun AppCompatTextView.bindOverview(overview: String?) {
            if (overview.isNotNullOrBlank()) {
                this.visibility = View.VISIBLE
                this.text = overview
            } else {
                this.visibility = View.GONE
            }
        }
    }

    private val viewModel: MovieViewModel by viewModels()

    private var _binding: ActivityMovieBinding? = null
    private val binding: ActivityMovieBinding
        get() = _binding!!

    private var _movieId: Int = -1
    private val movieId: Int
        get() = _movieId

    private var _movieTitle: String = ""
    private val movieTitle: String
        get() = _movieTitle

    private var _apiKey: String? = null
    private val apiKey: String
        get() = _apiKey!!

    private var _language: String? = null
    private val language: String
        get() = _language!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_movie)

        applyExtras()
        initKeys()

        binding.apply {
            lifecycleOwner = this@MovieActivity
            vm = viewModel

            initToolbar(this)
            initSlideShow(this)
        }

        viewModel.load(apiKey, language, movieId)
    }

    private fun applyExtras() {
        val movieIdExtra = intent.getIntExtra(MOVIE_ID_EXTRA_ID, -1)
        if (movieIdExtra != -1) {
            _movieId = movieIdExtra
        } else {
            finish()
        }

        val movieTitleExtra = intent.getStringExtra(MOVIE_TITLE_EXTRA_ID)
        _movieTitle = movieTitleExtra ?: resources.getString(R.string.app_name)
    }

    private fun initKeys() {
        val app = application as FilmpediaApp
        _apiKey = app.tmdbApiKey
        _language = app.language
    }

    private fun initToolbar(binding: ActivityMovieBinding) {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = movieTitle
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initSlideShow(binding: ActivityMovieBinding) {
        binding.apply {
            slideshow.apply {
                adapter = SlideShowAdapter()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

package io.woong.filmpedia.ui.page.movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.databinding.ActivityMovieBinding
import io.woong.filmpedia.ui.component.GenresTextView
import io.woong.filmpedia.ui.component.SeriesButton
import io.woong.filmpedia.ui.page.person.PersonActivity
import io.woong.filmpedia.ui.page.series.SeriesActivity
import io.woong.filmpedia.util.ListDecoration
import io.woong.filmpedia.util.UriUtil
import io.woong.filmpedia.util.isNotNullOrBlank
import io.woong.filmpedia.util.isNotNullOrEmpty
import java.lang.StringBuilder

class MovieActivity : AppCompatActivity(),
    View.OnClickListener,
    PeopleListAdapter.OnPeopleListClickListener,
    SeriesButton.OnSeriesButtonClickListener {

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
        @BindingAdapter("social_uri")
        fun AppCompatImageButton.bindButtonUri(uri: String?) {
            if (uri.isNotNullOrBlank()) {
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("text_or_gone")
        fun AppCompatTextView.bindTextOrGone(text: String?) {
            if (text.isNotNullOrBlank()) {
                if (this.visibility == View.GONE) {
                    this.visibility = View.VISIBLE
                }
                this.text = text
            } else {
                if (this.visibility == View.VISIBLE) {
                    this.visibility = View.GONE
                }
            }
        }

        @JvmStatic
        @BindingAdapter("director_and_casting")
        fun RecyclerView.bindDirectorAndCasting(people: List<PersonSummary>?) {
            if (people != null) {
                val adapter = this.adapter as PeopleListAdapter
                adapter.people = people
            }
        }

        @JvmStatic
        @BindingAdapter("movie_series")
        fun SeriesButton.bindMovieSeries(collection: Movie.Collection?) {
            if (collection != null) {
                this.visibility = View.VISIBLE
                this.series = collection
            } else {
                this.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("spoken_language_plural")
        fun AppCompatTextView.bindPlurals(list: List<String>?) {
            if (list.isNotNullOrEmpty()) {
                list!!
                this.text = this.resources.getQuantityText(R.plurals.movie_spoken_language, list.size)
            } else {
                this.text = this.resources.getQuantityText(R.plurals.movie_spoken_language, 2)
            }
        }

        @JvmStatic
        @BindingAdapter("spoken_languages")
        fun AppCompatTextView.bindSpokenLanguages(languages: List<String>?) {
            if (languages.isNotNullOrEmpty()) {
                languages!!
                val builder = StringBuilder(languages[0])
                for (index in 1 until languages.size) {
                    builder.append("\n${languages[index]}")
                }
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
        initBinding()

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

    private fun initBinding() {
        binding.apply {
            lifecycleOwner = this@MovieActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = movieTitle
            }

            slideshow.apply {
                adapter = SlideShowAdapter()
            }

            homepageButton.setOnClickListener(this@MovieActivity)
            facebookButton.setOnClickListener(this@MovieActivity)
            instagramButton.setOnClickListener(this@MovieActivity)
            twitterButton.setOnClickListener(this@MovieActivity)

            directorAndCasting.apply {
                adapter = PeopleListAdapter().apply {
                    listener = this@MovieActivity
                }
                layoutManager = LinearLayoutManager(this@MovieActivity, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(ListDecoration.HorizontalDecoration(16))
            }

            seriesButton.setOnSeriesButtonClickListener(this@MovieActivity)
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

    override fun onClick(v: View?) {
        var uri: Uri? = null

        when (v?.id) {
            binding.homepageButton.id -> uri = UriUtil.getSocialUri(viewModel.homepage.value, UriUtil.SocialType.OTHER)
            binding.facebookButton.id -> uri = UriUtil.getSocialUri(viewModel.facebook.value, UriUtil.SocialType.FACEBOOK)
            binding.instagramButton.id -> uri = UriUtil.getSocialUri(viewModel.instagram.value, UriUtil.SocialType.INSTAGRAM)
            binding.twitterButton.id -> uri = UriUtil.getSocialUri(viewModel.twitter.value, UriUtil.SocialType.TWITTER)
        }

        if (uri != null) {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onPeopleItemClick(person: PersonSummary?) {
        if (person != null) {
            val intent = Intent(this, PersonActivity::class.java)
            intent.putExtra(PersonActivity.PERSON_NAME_EXTRA_ID, person.name)
            intent.putExtra(PersonActivity.PERSON_ID_EXTRA_ID, person.id)
            startActivity(intent)
        }
    }

    override fun onFullButtonClick() {
        Snackbar.make(binding.root, "Not support yet.", Snackbar.LENGTH_LONG).show()
    }

    override fun onSeriesButtonClick(series: Movie.Collection?) {
        if (series != null) {
            val intent = Intent(this, SeriesActivity::class.java)
            intent.putExtra(SeriesActivity.COLLECTION_NAME_EXTRA_ID, series.name)
            intent.putExtra(SeriesActivity.COLLECTION_ID_EXTRA_ID, series.id)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

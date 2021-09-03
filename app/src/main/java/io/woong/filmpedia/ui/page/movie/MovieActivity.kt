package io.woong.filmpedia.ui.page.movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.databinding.ActivityMovieBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.component.SeriesButton
import io.woong.filmpedia.ui.page.people.PeopleActivity
import io.woong.filmpedia.ui.page.person.PersonActivity
import io.woong.filmpedia.ui.page.series.SeriesActivity
import io.woong.filmpedia.util.ListDecoration
import io.woong.filmpedia.util.UriUtil

class MovieActivity : BaseActivity<ActivityMovieBinding>(R.layout.activity_movie),
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
        @BindingAdapter("spoken_language_title_plural")
        fun AppCompatTextView.bindPlurals(list: List<String>?) {
            if (list != null) {
                this.text = this.resources.getQuantityText(R.plurals.movie_spoken_language, list.size)
            } else {
                this.text = this.resources.getQuantityText(R.plurals.movie_spoken_language, 2)
            }
        }
    }

    private val viewModel: MovieViewModel by viewModels()

    private var _movieId: Int = -1
    private val movieId: Int
        get() = _movieId

    private var _movieTitle: String = ""
    private val movieTitle: String
        get() = _movieTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyExtras()

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
        val intent = Intent(this, PeopleActivity::class.java)
        intent.putExtra(PeopleActivity.MOVIE_TITLE_EXTRA_ID, movieTitle)
        intent.putExtra(PeopleActivity.MOVIE_ID_EXTRA_ID, movieId)
        startActivity(intent)
    }

    override fun onSeriesButtonClick(series: Movie.Collection?) {
        if (series != null) {
            val intent = Intent(this, SeriesActivity::class.java)
            intent.putExtra(SeriesActivity.COLLECTION_NAME_EXTRA_ID, series.name)
            intent.putExtra(SeriesActivity.COLLECTION_ID_EXTRA_ID, series.id)
            startActivity(intent)
        }
    }
}

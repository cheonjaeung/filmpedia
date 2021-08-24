package io.woong.filmpedia.ui.page.moviedetail

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.*
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.databinding.ActivityMovieDetailBinding
import io.woong.filmpedia.ui.component.GenresTextView
import io.woong.filmpedia.ui.component.SeriesButton
import io.woong.filmpedia.ui.page.person.PersonActivity
import io.woong.filmpedia.ui.page.series.SeriesActivity
import io.woong.filmpedia.util.DimensionUtil
import io.woong.filmpedia.util.itemdeco.HorizontalItemDecoration
import io.woong.filmpedia.util.UriUtil
import io.woong.filmpedia.util.isNotNullOrEmpty
import java.lang.StringBuilder
import java.text.DecimalFormat

class MovieDetailActivity : AppCompatActivity(),
    View.OnClickListener,
    CreditListAdapter.OnCreditClickListener,
    SeriesButton.OnSeriesButtonClickListener,
    RecommendationListAdapter.OnRecommendationItemClickListener {

    companion object {
        const val MOVIE_ID_EXTRA_ID: String = "movie_id"
        const val MOVIE_TITLE_EXTRA_ID: String = "movie_title"
    }

    private val viewModel: MovieDetailViewModel by viewModels()
    private var _binding: ActivityMovieDetailBinding? = null
    private val binding: ActivityMovieDetailBinding
        get() = _binding!!

    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        val movieIdExtra = intent.getIntExtra(MOVIE_ID_EXTRA_ID, -1)
        if (movieIdExtra != -1) {
            movieId = movieIdExtra
        } else {
            finish()
        }

        val movieTitleExtra = intent.getStringExtra(MOVIE_TITLE_EXTRA_ID)
        val movieTitle = movieTitleExtra ?: resources.getString(R.string.app_name)

        binding.apply {
            lifecycleOwner = this@MovieDetailActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = movieTitle
            }

            homepageButton.setOnClickListener(this@MovieDetailActivity)
            facebookButton.setOnClickListener(this@MovieDetailActivity)
            instagramButton.setOnClickListener(this@MovieDetailActivity)
            twitterButton.setOnClickListener(this@MovieDetailActivity)
            tmdbButton.setOnClickListener(this@MovieDetailActivity)
            imdbButton.setOnClickListener(this@MovieDetailActivity)

            val itemDeco = HorizontalItemDecoration(8, resources.displayMetrics)

            castList.apply {
                adapter = CreditListAdapter(context, CreditListAdapter.Mod.CAST).apply {
                    setOnCreditClickListener(this@MovieDetailActivity)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            crewList.apply {
                adapter = CreditListAdapter(context, CreditListAdapter.Mod.CREW).apply {
                    setOnCreditClickListener(this@MovieDetailActivity)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            series.setOnSeriesButtonClickListener(this@MovieDetailActivity)

            recommendationsList.apply {
                adapter = RecommendationListAdapter(context).apply {
                    setOnRecommendationItemClickListener(this@MovieDetailActivity)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }
        }

        val app = application as FilmpediaApp
        viewModel.update(app.tmdbApiKey, movieId, app.language)
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
            binding.homepageButton.id -> {
                val homepageUrl = viewModel.movie.value?.homepage
                uri = UriUtil.getSocialUri(homepageUrl, UriUtil.SocialType.OTHER)
            }
            binding.facebookButton.id -> {
                val facebookId = viewModel.socialIds.value?.facebookId
                uri = UriUtil.getSocialUri(facebookId, UriUtil.SocialType.FACEBOOK)
            }
            binding.instagramButton.id -> {
                val instagramId = viewModel.socialIds.value?.instagramId
                uri = UriUtil.getSocialUri(instagramId, UriUtil.SocialType.INSTAGRAM)
            }
            binding.twitterButton.id -> {
                val twitterId = viewModel.socialIds.value?.twitterId
                uri = UriUtil.getSocialUri(twitterId, UriUtil.SocialType.TWITTER)
            }
            binding.tmdbButton.id -> {
                val tmdbId = viewModel.movie.value?.id
                uri = UriUtil.getSocialUri(tmdbId.toString(), UriUtil.SocialType.TMDB)
            }
            binding.imdbButton.id -> {
                val imdbId = viewModel.socialIds.value?.imdbId
                uri = UriUtil.getSocialUri(imdbId, UriUtil.SocialType.IMDB)
            }
        }

        uri?.let { u ->
            val intent = Intent(Intent.ACTION_VIEW, u)
            startActivity(intent)
        }
    }

    override fun onCreditClick(mod: CreditListAdapter.Mod, credit: Credits.CreditsSubItem) {
        val intent = Intent(this, PersonActivity::class.java)

        when (mod) {
            CreditListAdapter.Mod.CAST -> {
                credit as Credits.Cast
                intent.putExtra(PersonActivity.PERSON_ID_EXTRA_ID, credit.id)
                intent.putExtra(PersonActivity.PERSON_NAME_EXTRA_ID, credit.name)
            }
            CreditListAdapter.Mod.CREW -> {
                credit as Credits.Crew
                intent.putExtra(PersonActivity.PERSON_ID_EXTRA_ID, credit.id)
                intent.putExtra(PersonActivity.PERSON_NAME_EXTRA_ID, credit.name)
            }
        }

        startActivity(intent)
    }

    override fun onSeriesButtonClick(series: Movie.Collection?) {
        series?.let { s ->
            val intent = Intent(this@MovieDetailActivity, SeriesActivity::class.java)
            intent.putExtra(SeriesActivity.COLLECTION_ID_EXTRA_ID, s.id)
            intent.putExtra(SeriesActivity.COLLECTION_NAME_EXTRA_ID, s.name)
            startActivity(intent)
        }
    }

    override fun onRecommendationItemClick(position: Int, movies: List<Movies.Movie>) {
        if (position != RecyclerView.NO_POSITION) {
            val intent = Intent(this@MovieDetailActivity, MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_ID_EXTRA_ID, movies[position].id)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("movie_detail_backdrop_path")
fun AppCompatImageView.bindBackdropPath(path: String?) {
    path?.let { p ->
        Glide.with(this)
            .load(UriUtil.getImageUrl(p))
            .into(this)
    }
}

@BindingAdapter("movie_detail_poster_path")
fun AppCompatImageView.bindPosterPath(path: String?) {
    path?.let { p ->
        val radiusDp = DimensionUtil.dpToPx(8, context.resources.displayMetrics)

        Glide.with(this)
            .load(UriUtil.getImageUrl(p))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
            .placeholder(R.drawable.placeholder_poster)
            .into(this)
    }
}

@BindingAdapter("movie_detail_genres")
fun GenresTextView.bindGenres(genres: List<Genres.Genre>?) {
    genres?.let { g ->
        this.genres = g
    }
}

@BindingAdapter("movie_detail_runtime")
fun AppCompatTextView.bindRuntime(runtime: Int?) {
    runtime?.let { t ->
        val builder = StringBuilder("${runtime}m")
        var time = t

        val hours = time / 60
        time %= 60
        builder.append(" ")
        builder.append("(${hours}h ${time}m)")

        text = builder.toString()
    }
}

@BindingAdapter("movie_detail_enabled")
fun AppCompatImageButton.bindEnabled(enable: Boolean) {
    enable.let {
        if (it) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}

@BindingAdapter("movie_detail_languages")
fun AppCompatTextView.bindLanguages(languages: List<Movie.Country>?) {
    languages?.let { l ->
        val builder = StringBuilder()
        for ((index, lang) in l.withIndex()) {
            builder.append(lang.englishName)
            if (index < l.lastIndex) {
                builder.append("\n")
            }
        }
        this.text = builder.toString()
    }
}

@BindingAdapter("movie_detail_production_countries")
fun AppCompatTextView.bindProductionCountries(countries: List<Movie.Country>?) {
    countries?.let { c ->
        val builder = StringBuilder()
        for ((index, country) in c.withIndex()) {
            builder.append(country.name)
            if (index < c.lastIndex) {
                builder.append("\n")
            }
        }
        this.text = builder.toString()
    }
}

@BindingAdapter("movie_detail_production_companies")
fun AppCompatTextView.bindProductionCompanies(companies: List<Movie.Company>?) {
    companies?.let { c ->
        val builder = StringBuilder()
        for ((index, company) in c.withIndex()) {
            builder.append(company.name)
            if (index < c.lastIndex) {
                builder.append("\n")
            }
        }
        this.text = builder.toString()
    }
}

@BindingAdapter("movie_detail_money")
fun AppCompatTextView.bindMoney(money: Int?) {
    money?.let { m ->
        if (m <= 0) {
            this.text = "-"
        } else {
            val pattern = DecimalFormat("$ #,###")
            this.text = pattern.format(m)
        }
    }
}

@BindingAdapter("movie_detail_casts")
fun RecyclerView.bindCasts(casts: List<Credits.Cast>?) {
    casts?.let { c ->
        val adapter = this.adapter as CreditListAdapter
        adapter.credits = c
    }
}

@BindingAdapter("movie_detail_crews")
fun RecyclerView.bindCrews(crews: List<Credits.Crew>?) {
    crews?.let { c ->
        val adapter = this.adapter as CreditListAdapter
        adapter.credits = c
    }
}

@BindingAdapter("movie_detail_series")
fun SeriesButton.bindSeries(series: Movie.Collection?) {
    if (series != null) {
        if (this.visibility == View.GONE) {
            this.visibility = View.VISIBLE
        }
        this.series = series
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter("movie_detail_recommendation_title_enabled")
fun AppCompatTextView.bindRecommendationsTitleEnabled(movies: List<Movies.Movie>?) {
    if (movies.isNotNullOrEmpty()) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter("movie_detail_recommendation_movies")
fun RecyclerView.bindRecommendationMovies(movies: List<Movies.Movie>?) {
    if (movies.isNotNullOrEmpty()) {
        this.visibility = View.VISIBLE
        val adapter = this.adapter as RecommendationListAdapter
        adapter.movies = movies!!
    } else {
        this.visibility = View.GONE
    }
}

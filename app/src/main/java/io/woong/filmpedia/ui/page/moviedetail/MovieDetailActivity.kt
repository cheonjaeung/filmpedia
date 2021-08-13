package io.woong.filmpedia.ui.page.moviedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
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
import io.woong.filmpedia.R
import io.woong.filmpedia.adapter.CreditListAdapter
import io.woong.filmpedia.adapter.ProductionCompanyListAdapter
import io.woong.filmpedia.adapter.RecommendationListAdapter
import io.woong.filmpedia.data.Credits
import io.woong.filmpedia.data.Genre
import io.woong.filmpedia.data.Movie
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.databinding.ActivityMovieDetailBinding
import io.woong.filmpedia.ui.component.GenresTextView
import io.woong.filmpedia.util.HorizontalItemDecoration
import io.woong.filmpedia.util.ImagePathUtil
import java.lang.StringBuilder
import java.text.DecimalFormat

class MovieDetailActivity : AppCompatActivity(),
    View.OnClickListener,
    RecommendationListAdapter.OnRecommendationItemClickListener {

    companion object {
        const val MOVIE_ID_EXTRA_ID: String = "movie_id"
    }

    private val viewModel: MovieDetailViewModel by viewModels()
    private var _binding: ActivityMovieDetailBinding? = null
    private val binding: ActivityMovieDetailBinding
        get() = _binding!!

    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.icon_back)
        }

        val extra = intent.getIntExtra(MOVIE_ID_EXTRA_ID, -1)
        if (extra != -1) {
            movieId = extra
        } else {
            finish()
        }

        binding.apply {
            lifecycleOwner = this@MovieDetailActivity
            vm = viewModel

            homepageButton.setOnClickListener(this@MovieDetailActivity)
            facebookButton.setOnClickListener(this@MovieDetailActivity)
            instagramButton.setOnClickListener(this@MovieDetailActivity)
            twitterButton.setOnClickListener(this@MovieDetailActivity)
            tmdbButton.setOnClickListener(this@MovieDetailActivity)
            imdbButton.setOnClickListener(this@MovieDetailActivity)

            val itemDeco = HorizontalItemDecoration(8, resources.displayMetrics)

            castList.apply {
                adapter = CreditListAdapter(context, CreditListAdapter.Mod.CAST)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            crewList.apply {
                adapter = CreditListAdapter(context, CreditListAdapter.Mod.CREW)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            productionCompanyList.apply {
                adapter = ProductionCompanyListAdapter(context)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }

            recommendationsList.apply {
                adapter = RecommendationListAdapter(context).apply {
                    setOnRecommendationItemClickListener(this@MovieDetailActivity)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDeco)
            }
        }

        viewModel.update(movieId)
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
                homepageUrl?.let { url ->
                    uri = Uri.parse(url)
                }
            }
            binding.facebookButton.id -> {
                val facebookId = viewModel.socialIds.value?.facebookId
                facebookId?.let { id ->
                    uri = Uri.parse("https://www.facebook.com/$id")
                }
            }
            binding.instagramButton.id -> {
                val instagramId = viewModel.socialIds.value?.instagramId
                instagramId?.let { id ->
                    uri = Uri.parse("https://www.instagram.com/$id")
                }
            }
            binding.twitterButton.id -> {
                val twitterId = viewModel.socialIds.value?.twitterId
                twitterId?.let { id ->
                    uri = Uri.parse("https://twitter.com/$id")
                }
            }
            binding.tmdbButton.id -> {
                val tmdbId = viewModel.movie.value?.id
                tmdbId?.let { id ->
                    uri = Uri.parse("https://www.themoviedb.org/movie/$id")
                }
            }
            binding.imdbButton.id -> {
                val imdbId = viewModel.socialIds.value?.imdbId
                imdbId?.let { id ->
                    uri = Uri.parse("https://www.imdb.com/title/$id")
                }
            }
        }

        uri?.let { u ->
            val intent = Intent(Intent.ACTION_VIEW, u)
            startActivity(intent)
        }
    }

    override fun onRecommendationItemClick(position: Int, movies: List<Movies.Result>) {
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
            .load(ImagePathUtil.toFullUrl(p))
            .into(this)
    }
}

@BindingAdapter("movie_detail_poster_path")
fun AppCompatImageView.bindPosterPath(path: String?) {
    path?.let { p ->
        val radiusDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            context.resources.displayMetrics
        ).toInt()

        Glide.with(this)
            .load(ImagePathUtil.toFullUrl(p))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
            .placeholder(R.drawable.placeholder_poster)
            .into(this)
    }
}

@BindingAdapter("movie_detail_genres")
fun GenresTextView.bindGenres(genres: List<Genre>?) {
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

@BindingAdapter("movie_detail_production_companies")
fun RecyclerView.bindProductionCompanies(companies: List<Movie.Company>?) {
    companies?.let { c ->
        val adapter = this.adapter as ProductionCompanyListAdapter
        adapter.companies = c
    }
}

@BindingAdapter("movie_detail_recommendation_movies")
fun RecyclerView.bindRecommendationMovies(movies: List<Movies.Result>?) {
    movies?.let { m ->
        val adapter = this.adapter as RecommendationListAdapter
        adapter.movies = m
    }
}

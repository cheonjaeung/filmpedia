package io.woong.filmpedia.ui.page.moviedetail

import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Genre
import io.woong.filmpedia.databinding.ActivityMovieDetailBinding
import io.woong.filmpedia.ui.component.GenresTextView
import io.woong.filmpedia.util.ImagePathUtil
import java.lang.StringBuilder

class MovieDetailActivity : AppCompatActivity() {

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

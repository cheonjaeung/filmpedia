package io.woong.filmpedia.ui.page.series

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.collection.Collection
import io.woong.filmpedia.databinding.ActivitySeriesBinding
import io.woong.filmpedia.ui.page.movie.MovieActivity
import io.woong.filmpedia.util.ListDecoration
import io.woong.filmpedia.util.UriUtil
import io.woong.filmpedia.util.isNotNullOrBlank

class SeriesActivity : AppCompatActivity(), SeriesMovieListAdapter.OnSeriesMovieClickListener {

    companion object {
        const val COLLECTION_ID_EXTRA_ID: String = "collection_id"
        const val COLLECTION_NAME_EXTRA_ID: String = "collection_name"
    }

    private val viewModel: SeriesViewModel by viewModels()
    private var _binding: ActivitySeriesBinding? = null
    private val binding: ActivitySeriesBinding
        get() = _binding!!

    private var collectionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_series)

        val collectionIdExtra = intent.getIntExtra(COLLECTION_ID_EXTRA_ID, -1)
        if (collectionIdExtra != -1) {
            collectionId = collectionIdExtra
        } else {
            finish()
        }

        val collectionNameExtra = intent.getStringExtra(COLLECTION_NAME_EXTRA_ID)
        val collectionName = collectionNameExtra ?: resources.getString(R.string.app_name)

        binding.apply {
            lifecycleOwner = this@SeriesActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = collectionName
            }

            val deco = ListDecoration.VerticalDecoration(2)

            moviesList.apply {
                adapter = SeriesMovieListAdapter().apply {
                    listener = this@SeriesActivity
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(deco)
            }
        }

        val app = application as FilmpediaApp
        viewModel.update(app.tmdbApiKey, collectionId, app.language)
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

    override fun onSeriesMovieClick(position: Int, movies: List<Collection.Part>) {
        if (position != RecyclerView.NO_POSITION) {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(MovieActivity.MOVIE_ID_EXTRA_ID, movies[position].id)
            intent.putExtra(MovieActivity.MOVIE_TITLE_EXTRA_ID, movies[position].title)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("series_backdrop")
fun AppCompatImageView.bindBackdrop(path: String?) {
    if (path.isNotNullOrBlank()) {
        Glide.with(this)
            .load(UriUtil.getImageUrl(path))
            .placeholder(R.drawable.placeholder_backdrop)
            .into(this)
    }
}

@BindingAdapter("series_poster")
fun AppCompatImageView.bindPoster(path: String?) {
    if (path.isNotNullOrBlank()) {
        Glide.with(this)
            .load(UriUtil.getImageUrl(path))
            .placeholder(R.drawable.placeholder_poster)
            .into(this)
    }
}

@BindingAdapter("series_movies")
fun RecyclerView.bindMovies(movies: List<Collection.Part>?) {
    movies?.let { list ->
        val adapter = this.adapter as SeriesMovieListAdapter
        adapter.movies = list
    }
}

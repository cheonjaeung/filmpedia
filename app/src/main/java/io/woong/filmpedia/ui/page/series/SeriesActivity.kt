package io.woong.filmpedia.ui.page.series

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.R
import io.woong.filmpedia.data.collection.Collection
import io.woong.filmpedia.databinding.ActivitySeriesBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.page.movie.MovieActivity
import io.woong.filmpedia.util.ListDecoration

class SeriesActivity : BaseActivity<ActivitySeriesBinding>(R.layout.activity_series),
    SeriesMovieListAdapter.OnSeriesMovieClickListener {

    companion object {
        const val COLLECTION_ID_EXTRA_ID: String = "collection_id"
        const val COLLECTION_NAME_EXTRA_ID: String = "collection_name"
    }

    private val viewModel: SeriesViewModel by viewModels()

    private var _collectionId: Int = 0
    private val collectionId: Int
        get() = _collectionId

    private var _collectionTitle: String = ""
    private val collectionTitle: String
        get() = _collectionTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyExtras()

        binding.apply {
            lifecycleOwner = this@SeriesActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = collectionTitle
            }

            moviesList.apply {
                adapter = SeriesMovieListAdapter().apply {
                    listener = this@SeriesActivity
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(ListDecoration.VerticalDecoration(2))
            }
        }

        viewModel.load(apiKey, language, collectionId)
    }

    private fun applyExtras() {
        val collectionIdExtra = intent.getIntExtra(COLLECTION_ID_EXTRA_ID, -1)
        if (collectionIdExtra != -1) {
            _collectionId = collectionIdExtra
        } else {
            finish()
        }

        val collectionNameExtra = intent.getStringExtra(COLLECTION_NAME_EXTRA_ID)
        _collectionTitle = collectionNameExtra ?: resources.getString(R.string.app_name)
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
}

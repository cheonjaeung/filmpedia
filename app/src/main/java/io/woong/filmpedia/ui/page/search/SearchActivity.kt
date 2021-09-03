package io.woong.filmpedia.ui.page.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.search.SearchResult
import io.woong.filmpedia.databinding.ActivitySearchBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.page.movie.MovieActivity
import io.woong.filmpedia.util.GoToTopScrollListener
import io.woong.filmpedia.util.InfinityScrollListener
import io.woong.filmpedia.util.ListDecoration

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search),
    TextView.OnEditorActionListener,
    SearchResultListAdapter.OnSearchResultClickListener {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SearchActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = resources.getString(R.string.search)
            }

            searchBar.setOnEditorActionListener(this@SearchActivity)

            resultList.apply {
                adapter = SearchResultListAdapter().apply {
                    setOnSearchResultClickListener(this@SearchActivity)
                }
                layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(ListDecoration.VerticalDecoration(2))
                addOnScrollListener(
                    InfinityScrollListener {
                        val app = application as FilmpediaApp
                        viewModel.searchNext(app.tmdbApiKey, app.language, app.region, searchBar.text.toString())
                    }
                )
                addOnScrollListener(
                    GoToTopScrollListener(goToTopButton, 10, 2000) {
                        this.smoothScrollToPosition(0)
                    }
                )
            }
        }

        viewModel.updateGenres(apiKey, language)
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

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if (v?.id == binding.searchBar.id) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val app = application as FilmpediaApp
                viewModel.search(app.tmdbApiKey, app.language, app.region, v.text.toString())
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun onSearchResultClick(result: SearchResult?) {
        if (result != null) {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(MovieActivity.MOVIE_ID_EXTRA_ID, result.id)
            intent.putExtra(MovieActivity.MOVIE_TITLE_EXTRA_ID, result.title)
            startActivity(intent)
        }
    }
}

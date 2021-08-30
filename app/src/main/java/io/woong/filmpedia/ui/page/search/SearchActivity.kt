package io.woong.filmpedia.ui.page.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.search.SearchResult
import io.woong.filmpedia.databinding.ActivitySearchBinding
import io.woong.filmpedia.ui.page.moviedetail.MovieDetailActivity
import io.woong.filmpedia.util.GoToTopScrollListener
import io.woong.filmpedia.util.InfinityScrollListener
import io.woong.filmpedia.util.ListDecoration

class SearchActivity : AppCompatActivity(),
    TextView.OnEditorActionListener,
    SearchResultListAdapter.OnSearchResultClickListener {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: ActivitySearchBinding? = null
    private val binding: ActivitySearchBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

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

        val app = application as FilmpediaApp
        viewModel.updateGenres(app.tmdbApiKey, app.language)
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
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA_ID, result.id)
            intent.putExtra(MovieDetailActivity.MOVIE_TITLE_EXTRA_ID, result.title)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("search_results")
fun RecyclerView.bindSearchResults(searchResults: List<SearchResult>?) {
    if (searchResults != null) {
        if (searchResults.isNotEmpty()) {
            val adapter = this.adapter as SearchResultListAdapter
            adapter.results = searchResults
        }
    }
}

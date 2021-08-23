package io.woong.filmpedia.ui.page.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
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
import io.woong.filmpedia.adapter.SearchResultListAdapter
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.databinding.ActivitySearchBinding
import io.woong.filmpedia.ui.page.moviedetail.MovieDetailActivity
import io.woong.filmpedia.util.itemdeco.VerticalItemDecoration

class SearchActivity : AppCompatActivity(),
    View.OnClickListener,
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

            backButton.setOnClickListener(this@SearchActivity)

            searchBar.setOnEditorActionListener(this@SearchActivity)

            val listItemDeco = VerticalItemDecoration(1, resources.displayMetrics)

            resultList.apply {
                adapter = SearchResultListAdapter().apply {
                    setOnSearchResultClickListener(this@SearchActivity)
                }
                layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(listItemDeco)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.backButton.id) {
            finish()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if (v?.id == binding.searchBar.id) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val app = application as FilmpediaApp
                viewModel.update(app.tmdbApiKey, app.language, app.region, v.text.toString())
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun onSearchResultClick(result: Movies.Movie?) {
        if (result != null) {
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA_ID, result.id)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("search_results")
fun RecyclerView.bindSearchResults(searchResults: List<Movies.Movie>?) {
    searchResults?.let { results ->
        if (results.isNotEmpty()) {
            val adapter = this.adapter as SearchResultListAdapter
            adapter.results = results
        }
    }
}

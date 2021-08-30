package io.woong.filmpedia.ui.page.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.search.SearchResult
import io.woong.filmpedia.databinding.ItemSearchResultBinding
import io.woong.filmpedia.util.UriUtil

class SearchResultListAdapter : RecyclerView.Adapter<SearchResultListAdapter.ViewHolder>() {

    private val _results: MutableList<SearchResult> = mutableListOf()
    var results: List<SearchResult>
        get() = _results
        set(value) {
            _results.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    private var listener: OnSearchResultClickListener? = null

    fun setOnSearchResultClickListener(listener: OnSearchResultClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = _results.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val movie = results[position]

            Glide.with(holder.itemView.context)
                .load(UriUtil.getImageUrl(movie.posterPath))
                .placeholder(R.drawable.placeholder_poster)
                .into(holder.binding.poster)

            holder.binding.apply {
                title.text = movie.title
                genres.genres = movie.genres
                rating.rating = movie.rating
            }
        }
    }

    inner class ViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.root.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val result = _results[adapterPosition]
                    listener?.onSearchResultClick(result)
                }
            }
        }
    }

    interface OnSearchResultClickListener {
        fun onSearchResultClick(result: SearchResult?)
    }
}

package io.woong.filmpedia.ui.page.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.util.ImagePathUtil

class SearchResultListAdapter : RecyclerView.Adapter<SearchResultListAdapter.ViewHolder>() {

    private val _results: MutableList<Movies.Movie> = mutableListOf()
    var results: List<Movies.Movie>
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
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val movie = results[position]

            Glide.with(holder.itemView.context)
                .load(ImagePathUtil.toFullUrl(movie.backdropPath))
                .placeholder(R.drawable.placeholder_backdrop)
                .into(holder.backdropView)

            holder.titleView.text = movie.title
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rootView: ViewGroup = itemView.findViewById(R.id.search_result_list_item_root)
        val backdropView: AppCompatImageView = itemView.findViewById(R.id.backdrop)
        val titleView: AppCompatTextView = itemView.findViewById(R.id.title)

        init {
            rootView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == rootView.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val result = _results[adapterPosition]
                    listener?.onSearchResultClick(result)
                }
            }
        }
    }

    interface OnSearchResultClickListener {
        fun onSearchResultClick(result: Movies.Movie?)
    }
}

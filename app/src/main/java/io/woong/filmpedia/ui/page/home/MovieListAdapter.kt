package io.woong.filmpedia.ui.page.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.databinding.ItemHomeMovieListBinding
import io.woong.filmpedia.databinding.ItemHomeMovieListHeaderBinding
import io.woong.filmpedia.util.UriUtil

class MovieListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER: Int = 0
        private const val VIEW_TYPE_ITEM: Int = 1
    }

    var headerTitle: String = ""
    private val _items: MutableList<Movies.Movie> = mutableListOf()
    var items: List<Movies.Movie>
        get() = _items
        set(value) {
            _items.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = _items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemHomeMovieListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemHomeMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            if (holder is HeaderViewHolder) {
                holder.binding.apply {
                    title.text = headerTitle
                }
            } else if (holder is ItemViewHolder) {
                val item = items[position - 1]

                Glide.with(holder.itemView.context)
                    .load(UriUtil.getImageUrl(item.posterPath))
                    .placeholder(R.drawable.placeholder_poster)
                    .into(holder.binding.poster)
            }
        }
    }

    inner class HeaderViewHolder(val binding: ItemHomeMovieListHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ItemViewHolder(val binding: ItemHomeMovieListBinding) : RecyclerView.ViewHolder(binding.root)
}

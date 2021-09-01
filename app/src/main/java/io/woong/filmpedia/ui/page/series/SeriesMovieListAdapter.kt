package io.woong.filmpedia.ui.page.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.collection.Collection
import io.woong.filmpedia.databinding.ItemSeriesMovieListBinding
import io.woong.filmpedia.util.UriUtil

class SeriesMovieListAdapter : RecyclerView.Adapter<SeriesMovieListAdapter.ViewHolder>() {

    private val _movies: MutableList<Collection.Part> = mutableListOf()
    var movies: List<Collection.Part>
        get() = _movies
        set(value) {
            _movies.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    var listener: OnSeriesMovieClickListener? = null

    override fun getItemCount(): Int = _movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSeriesMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        if (position != RecyclerView.NO_POSITION) {
            val movie = movies[position]

            Glide.with(holder.itemView.context)
                .load(UriUtil.getImageUrl(movie.posterPath))
                .placeholder(R.drawable.placeholder_poster)
                .into(holder.binding.poster)

            holder.binding.apply {
                title.text = movie.title
                releaseDate.text = movie.releaseDate
                overview.text = movie.overview
            }
        }
    }

    inner class ViewHolder(val binding: ItemSeriesMovieListBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.itemRoot.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.itemRoot.id) {
                listener?.onSeriesMovieClick(adapterPosition, movies)
            }
        }
    }

    interface OnSeriesMovieClickListener {
        fun onSeriesMovieClick(position: Int, movies: List<Collection.Part>)
    }
}

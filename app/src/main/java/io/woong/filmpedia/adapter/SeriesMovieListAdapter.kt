package io.woong.filmpedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Collection
import io.woong.filmpedia.util.ImagePathUtil

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

    private var listener: OnSeriesMovieClickListener? = null

    fun setOnSeriesMovieClickListener(listener: OnSeriesMovieClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = _movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_series_movie_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        if (position != RecyclerView.NO_POSITION) {
            val movie = movies[position]

            Glide.with(holder.itemView.context)
                .load(ImagePathUtil.toFullUrl(movie.posterPath))
                .placeholder(R.drawable.placeholder_poster)
                .into(holder.posterView)

            holder.apply {
                titleView.text = movie.title
                releaseDateView.text = movie.releaseDate
                overviewView.text = movie.overview
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rootView: ViewGroup = itemView.findViewById(R.id.root)
        val posterView: AppCompatImageView = itemView.findViewById(R.id.poster)
        val titleView: AppCompatTextView = itemView.findViewById(R.id.title)
        val releaseDateView: AppCompatTextView = itemView.findViewById(R.id.release_date)
        val overviewView: AppCompatTextView = itemView.findViewById(R.id.overview)

        init {
            rootView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == rootView.id) {
                listener?.onSeriesMovieClick(adapterPosition, movies)
            }
        }
    }

    interface OnSeriesMovieClickListener {
        fun onSeriesMovieClick(position: Int, movies: List<Collection.Part>)
    }
}

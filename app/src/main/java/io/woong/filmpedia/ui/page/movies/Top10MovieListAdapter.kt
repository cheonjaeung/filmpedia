package io.woong.filmpedia.ui.page.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.ui.component.CircularRatingView
import io.woong.filmpedia.util.DimensionUtil
import io.woong.filmpedia.util.ImagePathUtil

class Top10MovieListAdapter(private val context: Context) : RecyclerView.Adapter<Top10MovieListAdapter.ViewHolder>() {

    private val top10: MutableList<Movies.Movie> = mutableListOf()
    private var itemClickListener: OnItemClickListener? = null
    private var ratingEnabled: Boolean = true

    fun setTop10(top10: List<Movies.Movie>) {
        this.top10.clear()
        this.top10.addAll(top10)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun setRatingEnabled(flag: Boolean) {
        ratingEnabled = flag
    }

    override fun getItemCount(): Int = top10.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.layout_top10_movie_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = top10[position]

        val radiusDp = DimensionUtil.dpToPx(4, context.resources.displayMetrics)

        Glide.with(context)
            .load(ImagePathUtil.toFullUrl(movie.posterPath))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
            .placeholder(R.drawable.placeholder_poster)
            .into(holder.posterView)

        holder.ratingView.rating = movie.voteAverage
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val posterView: AppCompatImageView = itemView.findViewById(R.id.t10mliv_poster)
        val ratingView: CircularRatingView = itemView.findViewById(R.id.t10mliv_rating)

        init {
            posterView.setOnClickListener(this)

            if (!ratingEnabled) {
                ratingView.visibility = View.GONE
            }
        }

        override fun onClick(v: View?) {
            if (v?.id == posterView.id) {
                itemClickListener?.onItemClick(adapterPosition, top10)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, movies: List<Movies.Movie>)
    }
}

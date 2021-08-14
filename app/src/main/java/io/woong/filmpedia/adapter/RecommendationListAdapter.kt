package io.woong.filmpedia.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.util.ImagePathUtil

class RecommendationListAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecommendationListAdapter.ViewHolder>(){

    private val _movies: MutableList<Movies.Movie> = mutableListOf()
    var movies: List<Movies.Movie>
        get() = _movies
        set(value) {
            _movies.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }
    private var listener: OnRecommendationItemClickListener? = null

    fun setOnRecommendationItemClickListener(listener: OnRecommendationItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = _movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.layout_recommendations_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val movie = movies[position]

            val radiusDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                context.resources.displayMetrics
            ).toInt()

            Glide.with(context)
                .load(ImagePathUtil.toFullUrl(movie.backdropPath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
                .placeholder(R.drawable.placeholder_poster)
                .into(holder.backdropView)

            holder.titleView.text = movie.title
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val rootView: ViewGroup = itemView.findViewById(R.id.recommendations_list_item_root)
        val backdropView: AppCompatImageView = itemView.findViewById(R.id.backdrop)
        val titleView: AppCompatTextView = itemView.findViewById(R.id.title)

        init {
            rootView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == rootView.id) {
                listener?.onRecommendationItemClick(adapterPosition, movies)
            }
        }
    }

    interface OnRecommendationItemClickListener {
        fun onRecommendationItemClick(position: Int, movies: List<Movies.Movie>)
    }
}

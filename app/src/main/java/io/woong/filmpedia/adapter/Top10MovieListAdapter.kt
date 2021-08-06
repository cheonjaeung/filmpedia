package io.woong.filmpedia.adapter

import android.content.Context
import android.util.TypedValue
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
import io.woong.filmpedia.util.ImagePathUtil

class Top10MovieListAdapter(private val context: Context) : RecyclerView.Adapter<Top10MovieListAdapter.ViewHolder>() {

    private val top10: MutableList<Movies.Result> = mutableListOf()

    private var itemClickListener: OnItemClickListener? = null

    fun setTop10(top10: List<Movies.Result>) {
        this.top10.clear()
        this.top10.addAll(top10)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int = top10.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.layout_top10_movie_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posterPath = top10[position].posterPath
        posterPath?.let { path ->
            val radiusDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                context.resources.displayMetrics
            ).toInt()

            Glide.with(context)
                .load(ImagePathUtil.toFullUrl(path))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
                .into(holder.posterView)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val posterView: AppCompatImageView = itemView.findViewById(R.id.t10mliv_poster)

        init {
            posterView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == posterView.id) {
                itemClickListener?.onItemClick(adapterPosition, top10)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, movies: List<Movies.Result>)
    }
}

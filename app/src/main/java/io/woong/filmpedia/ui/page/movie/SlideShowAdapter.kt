package io.woong.filmpedia.ui.page.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ItemMovieSlideshowBinding
import io.woong.filmpedia.util.UriUtil

class SlideShowAdapter : RecyclerView.Adapter<SlideShowAdapter.ViewHolder>() {

    private val _imagePaths: MutableList<String> = mutableListOf()
    var imagePaths: List<String>
        get() = _imagePaths
        set(value) {
            _imagePaths.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = _imagePaths.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieSlideshowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val imagePath = imagePaths[position]

            Glide.with(holder.itemView.context)
                .load(UriUtil.getImageUrl(imagePath))
                .placeholder(R.drawable.placeholder_backdrop)
                .into(holder.binding.slide)
        }
    }

    inner class ViewHolder(val binding: ItemMovieSlideshowBinding) : RecyclerView.ViewHolder(binding.root)
}

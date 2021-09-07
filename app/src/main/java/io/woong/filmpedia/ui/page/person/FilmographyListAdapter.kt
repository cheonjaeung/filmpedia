package io.woong.filmpedia.ui.page.person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.data.people.Filmography
import io.woong.filmpedia.databinding.ItemPersonFilmographyBinding
import io.woong.filmpedia.databinding.ItemPersonFilmographyDividerBinding
import io.woong.filmpedia.ui.component.FilmographyTextView

class FilmographyListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_VIEW_TYPE: Int = 0
        const val DIVIDER_VIEW_TYPE: Int = 1
    }

    private val _items: MutableList<Filmography> = mutableListOf()
    var items: List<Filmography>
        get() = _items
        set(value) {
            _items.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    var type: FilmographyTextView.TextType = FilmographyTextView.TextType.ACTING

    override fun getItemViewType(position: Int): Int {
        return if (items[position].viewType == ITEM_VIEW_TYPE) {
            ITEM_VIEW_TYPE
        } else {
            DIVIDER_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE) {
            val binding = ItemPersonFilmographyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding = ItemPersonFilmographyDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DividerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = items[position]

            if (item.viewType == ITEM_VIEW_TYPE) {
                holder as ItemViewHolder

                holder.binding.apply {
                    year.text = item.releasedYear
                    info.apply {
                        this.movieTitle = item.movieTitle
                        this.department = item.department
                    }
                }
            }
        }
    }

    inner class ItemViewHolder(val binding: ItemPersonFilmographyBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.info.textType = type
        }
    }

    inner class DividerViewHolder(val binding: ItemPersonFilmographyDividerBinding) : RecyclerView.ViewHolder(binding.root)
}

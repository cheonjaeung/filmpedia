package io.woong.filmpedia.ui.page.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.databinding.ItemMovieDirectorAndCastingBinding
import io.woong.filmpedia.databinding.ItemMovieDirectorAndCastingFooterBinding
import io.woong.filmpedia.util.UriUtil

class PeopleListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_VIEW_TYPE: Int = 0
        private const val FOOTER_VIEW_TYPE: Int = 1
        private const val FOOTER_COUNT: Int = 1
    }

    private val _people: MutableList<PersonSummary> = mutableListOf()
    var people: List<PersonSummary>
        get() = _people
        set(value) {
            _people.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    var listener: OnPeopleListClickListener? = null

    override fun getItemCount(): Int = _people.size + FOOTER_COUNT

    override fun getItemViewType(position: Int): Int {
        return if (position.isFooterPosition()) {
            FOOTER_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE) {
            val binding = ItemMovieDirectorAndCastingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding = ItemMovieDirectorAndCastingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            if (position.isNotFooterPosition()) {
                holder as ItemViewHolder
                val person = people[position]

                holder.binding.apply {
                    Glide.with(holder.itemView.context)
                        .load(UriUtil.getImageUrl(person.profilePath))
                        .placeholder(R.drawable.placeholder_profile)
                        .into(profile)

                    name.text = person.name

                    if (person.subtitle == "Director") {
                        val directorString = holder.itemView.context.getString(R.string.movie_director)
                        subtitle.text = directorString
                    } else {
                        subtitle.text = person.subtitle
                    }
                }
            }
        }
    }

    private fun Int.isFooterPosition(): Boolean = this == people.lastIndex + 1

    private fun Int.isNotFooterPosition(): Boolean = !this.isFooterPosition()

    inner class ItemViewHolder(val binding: ItemMovieDirectorAndCastingBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.itemRoot.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.itemRoot.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onPeopleItemClick(people[adapterPosition])
                }
            }
        }
    }

    inner class FooterViewHolder(val binding: ItemMovieDirectorAndCastingFooterBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.itemRoot.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.itemRoot.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onFullButtonClick()
                }
            }
        }
    }

    interface OnPeopleListClickListener {
        fun onPeopleItemClick(person: PersonSummary?)
        fun onFullButtonClick()
    }
}

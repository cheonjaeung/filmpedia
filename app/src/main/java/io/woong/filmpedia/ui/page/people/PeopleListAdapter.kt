package io.woong.filmpedia.ui.page.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.databinding.ItemPeopleListBinding
import io.woong.filmpedia.util.UriUtil

class PeopleListAdapter : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {

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

    var listener: OnPeopleListItemClickListener? = null

    override fun getItemCount(): Int = _people.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPeopleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val person = people[position]

            Glide.with(holder.itemView)
                .load(UriUtil.getImageUrl(person.profilePath))
                .placeholder(R.drawable.placeholder_profile)
                .into(holder.binding.profile)

            holder.binding.apply {
                name.text = person.name
                subtitle.text = person.subtitle
            }
        }
    }

    inner class ViewHolder(val binding: ItemPeopleListBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.itemRoot.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.itemRoot.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val person = people[adapterPosition]
                    listener?.onPeopleListItemClick(person)
                }
            }
        }
    }

    interface OnPeopleListItemClickListener {
        fun onPeopleListItemClick(person: PersonSummary?)
    }
}

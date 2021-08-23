package io.woong.filmpedia.ui.page.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.databinding.ItemPersonCreditsBinding
import io.woong.filmpedia.util.ImagePathUtil

class CreditListAdapter : RecyclerView.Adapter<CreditListAdapter.ViewHolder>() {

    private val _credits: MutableList<MovieCredits.Cast> = mutableListOf()
    var credits: List<MovieCredits.Cast>
        get() = _credits
        set(value) {
            _credits.apply {
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    private var listener: OnCreditClickListener? = null

    fun setOnCreditClickListener(listener: OnCreditClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = _credits.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val credit = credits[position]

            Glide.with(holder.itemView.context)
                .load(ImagePathUtil.toFullUrl(credit.posterPath))
                .placeholder(R.drawable.placeholder_poster)
                .into(holder.binding.poster)
        }
    }

    inner class ViewHolder(val binding: ItemPersonCreditsBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.poster.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.poster.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onCreditClick(credits[adapterPosition])
                }
            }
        }
    }

    interface OnCreditClickListener {
        fun onCreditClick(movie: MovieCredits.Cast?)
    }
}

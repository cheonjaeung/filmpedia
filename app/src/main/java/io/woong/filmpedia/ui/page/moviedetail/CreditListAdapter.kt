package io.woong.filmpedia.ui.page.moviedetail

import android.content.Context
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
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.util.DimensionUtil
import io.woong.filmpedia.util.UriUtil

class CreditListAdapter(private val context: Context, private val mod: Mod) : RecyclerView.Adapter<CreditListAdapter.ViewHolder>() {

    private val _credits: MutableList<Credits.CreditsSubItem> = mutableListOf()
    var credits: List<Credits.CreditsSubItem>
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
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.layout_credits_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val profilePath: String?
            val name: String
            val subtitle: String

            when (mod) {
                Mod.CAST -> {
                    val cast = _credits[position] as Credits.Cast
                    profilePath = cast.profilePath
                    name = cast.name
                    subtitle = cast.character
                }
                Mod.CREW -> {
                    val crew = _credits[position] as Credits.Crew
                    profilePath = crew.profilePath
                    name = crew.name
                    subtitle = crew.job
                }
            }

            val radiusDp = DimensionUtil.dpToPx(4, context.resources.displayMetrics)

            Glide.with(context)
                .load(UriUtil.getImageUrl(profilePath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
                .placeholder(R.drawable.placeholder_profile)
                .into(holder.profileView)

            holder.nameView.text = name
            holder.subtitleView.text = subtitle
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val rootView: ViewGroup = itemView.findViewById(R.id.cliv_root)
        val profileView: AppCompatImageView = itemView.findViewById(R.id.cliv_profile)
        val nameView: AppCompatTextView = itemView.findViewById(R.id.cliv_name)
        val subtitleView: AppCompatTextView = itemView.findViewById(R.id.cliv_subtitle)

        init {
            rootView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == rootView.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onCreditClick(mod, credits[adapterPosition])
                }
            }
        }
    }

    enum class Mod(val value: Int) {
        CAST(1),
        CREW(2)
    }

    interface OnCreditClickListener {
        fun onCreditClick(mod: Mod, credit: Credits.CreditsSubItem)
    }
}
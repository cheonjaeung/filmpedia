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
import io.woong.filmpedia.data.Movie
import io.woong.filmpedia.util.ImagePathUtil

class ProductionCompanyListAdapter(private val context: Context) : RecyclerView.Adapter<ProductionCompanyListAdapter.ViewHolder>() {

    private val _companies: MutableList<Movie.Company> = mutableListOf()
    var companies: List<Movie.Company>
        get() = _companies
        set(value) {
            _companies.apply {
                clear()
                addAll(value)
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int = _companies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.layout_production_company_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val company = companies[position]

            Glide.with(context)
                .load(ImagePathUtil.toFullUrl(company.logoPath))
                .placeholder(R.drawable.placeholder_company)
                .into(holder.logoView)

            holder.nameView.text = company.name
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val logoView: AppCompatImageView = itemView.findViewById(R.id.pcliv_logo)
        val nameView: AppCompatTextView = itemView.findViewById(R.id.pcliv_name)
    }
}

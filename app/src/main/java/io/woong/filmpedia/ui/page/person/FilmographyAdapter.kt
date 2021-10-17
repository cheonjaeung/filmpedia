package io.woong.filmpedia.ui.page.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.Filmography
import io.woong.filmpedia.data.people.Person
import io.woong.filmpedia.databinding.*
import io.woong.filmpedia.util.DateUtil
import io.woong.filmpedia.util.UriUtil
import java.lang.StringBuilder

class FilmographyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    var listener: OnMovieClickListener? = null

    enum class ViewType(val value: Int) {
        PROFILE(0),
        BIOGRAPHY(1),
        TITLE(2),
        MOVIE(3),
        DIVIDER(4)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType.value
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.PROFILE.value -> {
                val binding = ItemPersonFilmographyProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProfileViewHolder(binding)
            }

            ViewType.BIOGRAPHY.value -> {
                val binding = ItemPersonFilmographyBiographyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BiographyViewHolder(binding)
            }

            ViewType.TITLE.value -> {
                val binding = ItemPersonFilmographyTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TitleViewHolder(binding)
            }

            ViewType.MOVIE.value -> {
                val binding = ItemPersonFilmographyMovieBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieViewHolder(binding)
            }

            else -> {
                val binding = ItemPersonFilmographyDividerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DividerViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = items[position]

            when (item.viewType) {
                ViewType.PROFILE -> {
                    item as Filmography.Profile
                    holder as ProfileViewHolder

                    holder.binding.apply {

                        Glide.with(root)
                            .load(UriUtil.getImageUrl(item.profilePath))
                            .placeholder(R.drawable.placeholder_profile)
                            .into(profile)

                        name.text = item.name

                        setLife(life, item.birthday, item.deathday)

                        setGender(gender, item.gender)

                        if (item.placeOfBirth != null) {
                            placeOfBirthTitle.visibility = View.VISIBLE
                            placeOfBirth.visibility = View.VISIBLE
                            placeOfBirth.text = item.placeOfBirth
                        } else {
                            placeOfBirthTitle.visibility = View.GONE
                            placeOfBirth.visibility = View.GONE
                        }
                    }
                }

                ViewType.BIOGRAPHY -> {
                    item as Filmography.Biography
                    holder as BiographyViewHolder

                    holder.binding.apply {
                        if (item.content != null) {
                            biography.visibility = View.VISIBLE
                            biography.text = item.content
                        } else {
                            biography.visibility = View.GONE
                        }
                    }
                }

                ViewType.TITLE -> {
                    item as Filmography.Title
                    holder as TitleViewHolder

                    holder.binding.apply {
                        title.text = item.text
                    }
                }

                ViewType.MOVIE -> {
                    item as Filmography.Movie
                    holder as MovieViewHolder

                    holder.binding.apply {
                        year.text = item.releaseYear
                        info.apply {
                            movieTitle = item.title
                            department = item.department
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun setLife(view: AppCompatTextView, birthday: String?, deathday: String?) {
        if (birthday != null) {
            val age = DateUtil.getAge(birthday, deathday, -1)

            val builder = StringBuilder(birthday)

            if (deathday != null) {
                builder.append("~")
                builder.append(" ")
                builder.append("$deathday")
            }

            builder.append(" ")
            builder.append("($age)")

            view.visibility = View.VISIBLE
            view.text = builder.toString()
        } else {
            view.visibility = View.GONE
        }
    }

    private fun setGender(view: AppCompatTextView, gender: Person.Gender) {
        val genderString: String = when (gender) {
            Person.Gender.UNSPECIFIED -> view.resources.getString(R.string.person_gender_unspecified)
            Person.Gender.MALE -> view.resources.getString(R.string.person_gender_male)
            Person.Gender.FEMALE -> view.resources.getString(R.string.person_gender_female)
            Person.Gender.NON_BINARY -> view.resources.getString(R.string.person_gender_non_binary)
        }

        view.text = genderString
    }

    inner class ProfileViewHolder(
        val binding: ItemPersonFilmographyProfileBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class BiographyViewHolder(
        val binding: ItemPersonFilmographyBiographyBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class TitleViewHolder(
        val binding: ItemPersonFilmographyTitleBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class MovieViewHolder(
        val binding: ItemPersonFilmographyMovieBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.info.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id == binding.info.id) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = items[adapterPosition]
                    if (item is Filmography.Movie) {
                        listener?.onMovieClick(item)
                    }
                }
            }
        }
    }

    inner class DividerViewHolder(
        val binding: ItemPersonFilmographyDividerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface OnMovieClickListener {
        fun onMovieClick(item: Filmography.Movie)
    }
}

package io.woong.filmpedia.ui.page.person

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.databinding.ActivityPersonBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.page.movie.MovieActivity
import io.woong.filmpedia.util.ListDecoration
import io.woong.filmpedia.util.UriUtil
import io.woong.filmpedia.util.isNotNullOrEmpty
import java.lang.StringBuilder

class PersonActivity : BaseActivity<ActivityPersonBinding>(R.layout.activity_person),
    CreditListAdapter.OnCreditClickListener {

    companion object {
        const val PERSON_ID_EXTRA_ID: String = "person_id"
        const val PERSON_NAME_EXTRA_ID: String = "person_name"
    }

    private val viewModel: PersonViewModel by viewModels()

    private var personId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val personIdExtra = intent.getIntExtra(PERSON_ID_EXTRA_ID, -1)
        if (personIdExtra != -1) {
            personId = personIdExtra
        } else {
            finish()
        }

        val personNameExtra = intent.getStringExtra(PERSON_NAME_EXTRA_ID)
        val personName = personNameExtra ?: resources.getString(R.string.app_name)

        binding.apply {
            lifecycleOwner = this@PersonActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = personName
            }

            val lineSize = 3
            val deco = ListDecoration.GridDecoration(2)
            movieCreditList.apply {
                adapter = CreditListAdapter().apply {
                    setOnCreditClickListener(this@PersonActivity)
                }
                layoutManager = GridLayoutManager(this@PersonActivity, lineSize)
                addItemDecoration(deco)
            }
        }

        viewModel.update(personId, apiKey, language, region)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreditClick(movie: MovieCredits.Cast?) {
        if (movie != null) {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(MovieActivity.MOVIE_ID_EXTRA_ID, movie.id)
            intent.putExtra(MovieActivity.MOVIE_TITLE_EXTRA_ID, movie.title)
            startActivity(intent)
        }
    }
}

@BindingAdapter("person_profile")
fun AppCompatImageView.bindProfile(path: String?) {
    Glide.with(this)
        .load(UriUtil.getImageUrl(path))
        .placeholder(R.drawable.placeholder_profile)
        .into(this)
}

@BindingAdapter(value = ["person_birthday", "person_deathday", "person_age"])
fun AppCompatTextView.bindPersonAge(birthday: String?, deathday: String?, age: Int) {
    birthday?.let {
        val builder = StringBuilder(birthday)

        if (deathday != null) {
            builder.append(" - $deathday")
        }

        val ageUnit = this.context.resources.getQuantityString(R.plurals.year_old, age)
        builder.append(" ($age $ageUnit)")

        this.text = builder.toString()
    }
}

@BindingAdapter("person_casted_movies")
fun RecyclerView.bindPersonCastedMovies(credits: List<MovieCredits.Cast>?) {
    if (credits.isNotNullOrEmpty()) {
        credits as List<MovieCredits.Cast>
        val adapter = this.adapter as CreditListAdapter
        adapter.credits = credits
    }
}

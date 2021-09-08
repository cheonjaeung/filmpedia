package io.woong.filmpedia.ui.page.person

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.Filmography
import io.woong.filmpedia.databinding.ActivityPersonBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.page.movie.MovieActivity
import io.woong.filmpedia.util.AnimationUtil

class PersonActivity : BaseActivity<ActivityPersonBinding>(R.layout.activity_person),
    FilmographyAdapter.OnMovieClickListener {

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

            filmography.apply {
                adapter = FilmographyAdapter().apply {
                    listener = this@PersonActivity
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            AnimationUtil.blink(loading, 1000)
        }

        viewModel.load(this, personId, apiKey, language, region)
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

    override fun onMovieClick(item: Filmography.Movie) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra(MovieActivity.MOVIE_TITLE_EXTRA_ID, item.title)
        intent.putExtra(MovieActivity.MOVIE_ID_EXTRA_ID, item.id)
        startActivity(intent)
    }
}

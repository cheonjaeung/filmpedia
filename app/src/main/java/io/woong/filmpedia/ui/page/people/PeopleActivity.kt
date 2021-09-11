package io.woong.filmpedia.ui.page.people

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import io.woong.filmpedia.R
import io.woong.filmpedia.data.people.PersonSummary
import io.woong.filmpedia.databinding.ActivityPeopleBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.page.person.PersonActivity
import io.woong.filmpedia.util.AnimationUtil
import io.woong.filmpedia.util.ListDecoration

class PeopleActivity : BaseActivity<ActivityPeopleBinding>(R.layout.activity_people),
    View.OnClickListener,
    PeopleListAdapter.OnPeopleListItemClickListener{

    companion object {
        const val MOVIE_TITLE_EXTRA_ID: String = "movie_title"
        const val MOVIE_ID_EXTRA_ID: String = "movie_id"
    }

    private val viewModel: PeopleViewModel by viewModels()

    private var movieTitle: String = ""
    private var movieId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyExtras()

        binding.apply {
            lifecycleOwner = this@PeopleActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
                title = movieTitle
            }

            peopleList.apply {
                adapter = PeopleListAdapter().apply {
                    listener = this@PeopleActivity
                }
                layoutManager = GridLayoutManager(this@PeopleActivity, 2, GridLayoutManager.VERTICAL, false)
                addItemDecoration(ListDecoration.GridDecoration(2))
            }

            offline.loadAgain.setOnClickListener(this@PeopleActivity)

            AnimationUtil.blink(loading, 1000)
        }

        viewModel.load(apiKey, language, movieId)
    }

    private fun applyExtras() {
        movieTitle = intent.getStringExtra(MOVIE_TITLE_EXTRA_ID) ?: getString(R.string.app_name)

        val movieIdExtra = intent.getIntExtra(MOVIE_ID_EXTRA_ID, -1)
        if (movieIdExtra != -1) {
            movieId = movieIdExtra
        } else {
            finish()
        }
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

    override fun onClick(v: View?) {
        if (v?.id == binding.offline.loadAgain.id) {
            viewModel.load(apiKey, language, movieId)
        }
    }

    override fun onPeopleListItemClick(person: PersonSummary?) {
        if (person != null) {
            val intent = Intent(this, PersonActivity::class.java)
            intent.putExtra(PersonActivity.PERSON_NAME_EXTRA_ID, person.name)
            intent.putExtra(PersonActivity.PERSON_ID_EXTRA_ID, person.id)
            startActivity(intent)
        }
    }
}

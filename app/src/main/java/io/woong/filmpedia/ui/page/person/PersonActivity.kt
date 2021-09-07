package io.woong.filmpedia.ui.page.person

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ActivityPersonBinding
import io.woong.filmpedia.ui.base.BaseActivity
import io.woong.filmpedia.ui.component.FilmographyTextView
import io.woong.filmpedia.util.ListDecoration

class PersonActivity : BaseActivity<ActivityPersonBinding>(R.layout.activity_person) {

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

            initList(FilmographyTextView.TextType.ACTING, actingList)
            initList(FilmographyTextView.TextType.STAFF, directingList, otherList)
        }

        viewModel.update(personId, apiKey, language, region)
    }

    private fun initList(type: FilmographyTextView.TextType, vararg list: RecyclerView) {
        list.forEach {
            it.apply {
                adapter = FilmographyListAdapter().apply {
                    this.type = type
                }
                layoutManager = LinearLayoutManager(this@PersonActivity, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(ListDecoration.VerticalDecoration(8))
            }
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
}

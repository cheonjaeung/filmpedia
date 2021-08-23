package io.woong.filmpedia.ui.page.person

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import io.woong.filmpedia.FilmpediaApp
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Person
import io.woong.filmpedia.databinding.ActivityPersonBinding
import io.woong.filmpedia.util.ImagePathUtil
import java.lang.StringBuilder

class PersonActivity : AppCompatActivity() {

    companion object {
        const val PERSON_ID_EXTRA_ID: String = "person_id"
    }

    private val viewModel: PersonViewModel by viewModels()
    private var _binding: ActivityPersonBinding? = null
    private val binding: ActivityPersonBinding
        get() = _binding!!
    private var personId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_person)

        val extra = intent.getIntExtra(PERSON_ID_EXTRA_ID, -1)
        if (extra != -1) {
            personId = extra
        } else {
            finish()
        }

        binding.apply {
            lifecycleOwner = this@PersonActivity
            vm = viewModel

            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.icon_back)
            }
        }

        val app = application as FilmpediaApp
        viewModel.update(personId, app.tmdbApiKey, app.language)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("person_profile")
fun AppCompatImageView.bindProfile(path: String?) {
    Glide.with(this)
        .load(ImagePathUtil.toFullUrl(path))
        .placeholder(R.drawable.placeholder_profile)
        .into(this)
}

@BindingAdapter("person_birthday_and_deathday")
fun AppCompatTextView.bindPersonBirthdayAndDeathday(person: Person?) {
    if (person != null) {
        if (person.birthday != null) {
            val builder = StringBuilder(person.birthday)

            if (person.deathday != null) {
                builder.append(" - ")
                builder.append(person.deathday)
            }

            this.text = builder.toString()
        }
    }
}

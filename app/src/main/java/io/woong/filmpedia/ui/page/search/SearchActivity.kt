package io.woong.filmpedia.ui.page.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivitySearchBinding? = null
    private val binding: ActivitySearchBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.apply {
            lifecycleOwner = this@SearchActivity

            backButton.setOnClickListener(this@SearchActivity)
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.backButton.id) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

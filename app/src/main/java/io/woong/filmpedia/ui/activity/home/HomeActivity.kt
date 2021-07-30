package io.woong.filmpedia.ui.activity.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.woong.filmpedia.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.recommendedMovie.observe(this) {
            binding.recommendedMovie.movie = it
        }
        viewModel.update()
    }
}

package io.woong.filmpedia.ui.activity.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.repository.HomeRepository

class HomeViewModel : ViewModel() {

    private val repository: HomeRepository = HomeRepository()

    val recommendedMovie: MutableLiveData<RecommendedMovie> by lazy {
        MutableLiveData<RecommendedMovie>()
    }

    fun update() {
        repository.fetchRecommendedMovie { isSuccess, movie, error ->
            if (isSuccess) {
                recommendedMovie.value = movie
                Log.i("Test", movie.toString())
            }
        }
    }
}

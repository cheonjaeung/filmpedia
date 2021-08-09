package io.woong.filmpedia.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.databinding.LayoutMovieDetailBottomSheetBinding
import io.woong.filmpedia.util.ImagePathUtil

class MovieDetailBottomSheet(private val movie: Movies.Result) : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: LayoutMovieDetailBottomSheetBinding? = null
    private val binding: LayoutMovieDetailBottomSheetBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutMovieDetailBottomSheetBinding.inflate(inflater, container, false)

        binding.apply {
            movie.posterPath?.let { path ->
                Glide.with(binding.root)
                    .load(ImagePathUtil.toFullUrl(path))
                    .into(binding.poster)
            }

            title.text = movie.title
            releaseDate.text = movie.releaseDate
            adultMark.text = if (movie.adult) "Adult" else ""
            overview.text = movie.overview

            closeButton.setOnClickListener(this@MovieDetailBottomSheet)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.closeButton.id) {
            this.dismiss()
        }
    }
}

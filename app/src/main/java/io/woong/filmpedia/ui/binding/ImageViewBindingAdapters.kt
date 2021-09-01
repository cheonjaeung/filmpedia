package io.woong.filmpedia.ui.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.util.UriUtil

object ImageViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("poster")
    fun AppCompatImageView.bindPosterImage(posterPath: String?) {
        Glide.with(this)
            .load(UriUtil.getImageUrl(posterPath))
            .placeholder(R.drawable.placeholder_poster)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("profile")
    fun AppCompatImageView.bindProfileImage(posterPath: String?) {
        Glide.with(this)
            .load(UriUtil.getImageUrl(posterPath))
            .placeholder(R.drawable.placeholder_profile)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("backdrop")
    fun AppCompatImageView.bindBackdropImage(posterPath: String?) {
        Glide.with(this)
            .load(UriUtil.getImageUrl(posterPath))
            .placeholder(R.drawable.placeholder_backdrop)
            .into(this)
    }
}

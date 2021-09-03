package io.woong.filmpedia.ui.binding

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import io.woong.filmpedia.util.UriUtil

object ImageViewBindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["glide_image", "glide_placeholder"], requireAll = false)
    fun AppCompatImageView.bindGlideImage(imagePath: String?, placeholder: Drawable?) {
        if (placeholder != null) {
            Glide.with(this)
                .load(UriUtil.getImageUrl(imagePath))
                .placeholder(placeholder)
                .into(this)
        } else {
            Glide.with(this)
                .load(UriUtil.getImageUrl(imagePath))
                .into(this)
        }
    }
}

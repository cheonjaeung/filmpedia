package io.woong.filmpedia.ui.binding

import android.view.View
import androidx.databinding.BindingAdapter

object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("is_visible")
    fun View.bindVisible(isVisible: Boolean?) {
        if (isVisible != null) {
            if (isVisible) {
                if (this.visibility != View.VISIBLE) {
                    this.visibility = View.VISIBLE
                }
            } else {
                if (this.visibility != View.GONE) {
                    this.visibility = View.GONE
                }
            }
        }
    }
}

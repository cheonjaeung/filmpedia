package io.woong.filmpedia.ui.binding

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

object TextViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("money")
    fun bindMoney(view: AppCompatTextView, money: Long?) {
        if (money != null) {
            if (money > 0) {
                val pattern = DecimalFormat("$ #,###")
                view.text = pattern.format(money)
            } else {
                view.text = "-"
            }
        }
    }
}

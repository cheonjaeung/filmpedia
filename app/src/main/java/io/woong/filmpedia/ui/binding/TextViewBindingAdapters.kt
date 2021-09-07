package io.woong.filmpedia.ui.binding

import androidx.annotation.PluralsRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import io.woong.filmpedia.R
import java.lang.StringBuilder
import java.text.DecimalFormat

object TextViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("multiline_text")
    fun AppCompatTextView.bindMultilineText(textList: List<String>?) {
        if (textList != null) {
            val builder = StringBuilder()

            for (index in textList.indices) {
                if (index > 0) {
                    builder.append("\n${textList[index]}")
                } else {
                    builder.append(textList[index])
                }
            }

            this.text = builder.toString()
        }
    }

    @JvmStatic
    @BindingAdapter("runtime")
    fun AppCompatTextView.bindRuntime(runtime: Int?) {
        if (runtime != null) {
            val hours = runtime / 60
            val minutes = runtime % 60

            val runtimeString = resources.getString(R.string.movie_runtime, hours, minutes)

            this.text = runtimeString
        }
    }

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

    @JvmStatic
    @BindingAdapter(value = ["plurals_resource", "plurals_count", "plurals_list"], requireAll = false)
    fun AppCompatTextView.bindPlurals(@PluralsRes res: Int?, count: Int?, list: List<Any>?) {
        val c = list?.size ?: 2

        if (res != null) {
            val plurals = resources.getQuantityText(res, c)
            this.text = plurals
        }
    }
}

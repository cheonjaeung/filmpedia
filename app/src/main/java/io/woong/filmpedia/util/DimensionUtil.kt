package io.woong.filmpedia.util

import android.util.DisplayMetrics
import android.util.TypedValue

object DimensionUtil {

    fun dpToPx(dp: Float, displayMetrics: DisplayMetrics): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            displayMetrics
        )
    }

    fun dpToPx(dp: Int, displayMetrics: DisplayMetrics): Int {
        return dpToPx(dp.toFloat(), displayMetrics).toInt()
    }
}

package io.woong.filmpedia.util

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

object AnimationUtil {

    fun fadeIn(
        view: View,
        duration: Long,
        interpolator: Interpolator = LinearInterpolator(),
        startDelay: Long = 0
    ) {
        val animation = AlphaAnimation(0.0f, 1.0f).apply {
            this.interpolator = interpolator
            this.startOffset = startDelay
            this.duration = duration
        }
        view.startAnimation(animation)
    }

    fun fadeOut(
        view: View,
        duration: Long,
        interpolator: Interpolator = LinearInterpolator(),
        startDelay: Long = 0
    ) {
        val animation = AlphaAnimation(1.0f, 0.0f).apply {
            this.interpolator = interpolator
            this.startOffset = startDelay
            this.duration = duration
        }
        view.startAnimation(animation)
    }
}

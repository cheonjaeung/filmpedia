package io.woong.filmpedia.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GoToTopScrollListener(
    private val goToTopButton: View,
    private val minItemCount: Int,
    private val dismissDelay: Long,
    private val onGoToTopButtonClick: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var isShowing: Boolean = false

    init {
        goToTopButton.setOnClickListener {
            onGoToTopButtonClick()
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (recyclerView.adapter != null) {
            val adapter = recyclerView.adapter!!

            if (adapter.itemCount >= minItemCount) {
                if (dy.isScrollDown()) {
                    dismissButton()
                } else if (dy.isScrollUp()) {
                    showButton()
                }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState.isScrollIdle()) {
            dismissButton()
        }
    }

    private fun Int.isScrollUp(): Boolean = this < 0

    private fun Int.isScrollDown(): Boolean = this > 0

    private fun Int.isScrollIdle(): Boolean = this == RecyclerView.SCROLL_STATE_IDLE

    private fun showButton() {
        if (!isShowing) {
            isShowing = true
            goToTopButton.visibility = View.VISIBLE
            AnimationUtil.fadeIn(goToTopButton, 500)
        }
    }

    private fun dismissButton() {
        if (isShowing) {
            isShowing = false
            AnimationUtil.fadeOut(goToTopButton, 500, startDelay = dismissDelay)
            goToTopButton.visibility = View.GONE
        }
    }
}

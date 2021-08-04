package io.woong.filmpedia.util

import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(marginInDp: Int, matrix: DisplayMetrics) : RecyclerView.ItemDecoration() {

    private val marginDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp.toFloat(), matrix).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        parent.adapter?.let { adapter ->
            if (parent.getChildAdapterPosition(view) < adapter.itemCount - 1) {
                outRect.left = marginDp
            } else {
                outRect.left = marginDp
                outRect.right = marginDp
            }
        }
    }
}

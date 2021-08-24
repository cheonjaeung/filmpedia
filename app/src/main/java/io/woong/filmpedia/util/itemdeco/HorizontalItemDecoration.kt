package io.woong.filmpedia.util.itemdeco

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.util.DimensionUtil

class HorizontalItemDecoration(marginInDp: Int, matrix: DisplayMetrics) : RecyclerView.ItemDecoration() {

    private val marginDp = DimensionUtil.dpToPx(marginInDp, matrix)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = marginDp
    }
}

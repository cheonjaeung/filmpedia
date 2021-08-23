package io.woong.filmpedia.util.itemdeco

import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val lineSize: Int,
    marginInDp: Int,
    matrix: DisplayMetrics
) : RecyclerView.ItemDecoration() {

    private val marginDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp.toFloat(), matrix).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(marginDp / 2, marginDp / 2, marginDp / 2, marginDp / 2)
    }
}
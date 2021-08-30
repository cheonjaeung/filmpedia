package io.woong.filmpedia.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

sealed class ListDecoration : RecyclerView.ItemDecoration() {

    class HorizontalDecoration(private val margin: Int) : ListDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val margin = DimensionUtil.dpToPx(margin, view.context.resources.displayMetrics)

            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                outRect.right = margin
            }
        }
    }

    class VerticalDecoration(private val margin: Int) : ListDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val margin = DimensionUtil.dpToPx(margin, view.context.resources.displayMetrics)

            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                outRect.bottom = margin
            }
        }
    }

    class GridDecoration(private val margin: Int) : ListDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val margin = DimensionUtil.dpToPx(margin, view.context.resources.displayMetrics)

            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                outRect.apply {
                    left = margin / 2
                    top = margin / 2
                    right = margin / 2
                    bottom = margin / 2
                }
            }
        }
    }

    class GridWithHeaderDecoration(private val margin: Int) : ListDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val margin = DimensionUtil.dpToPx(margin, view.context.resources.displayMetrics)

            val position = parent.getChildAdapterPosition(view)

            if (position == 0) return

            if (position != RecyclerView.NO_POSITION) {
                outRect.apply {
                    left = margin / 2
                    top = margin / 2
                    right = margin / 2
                    bottom = margin / 2
                }
            }
        }
    }
}

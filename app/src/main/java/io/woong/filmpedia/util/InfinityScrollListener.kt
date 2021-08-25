package io.woong.filmpedia.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfinityScrollListener(private val onLastPosition: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (recyclerView.layoutManager != null && recyclerView.adapter != null) {
            val adapter = recyclerView.adapter!!
            val lm = when (recyclerView.layoutManager) {
                is LinearLayoutManager -> recyclerView.layoutManager as LinearLayoutManager
                is GridLayoutManager -> recyclerView.layoutManager as GridLayoutManager
                else -> return
            }

            val lastVisiblePosition = lm.findLastCompletelyVisibleItemPosition()
            val itemCount = adapter.itemCount

            if (lastVisiblePosition == itemCount - 1) {
                onLastPosition()
            }
        }
    }
}

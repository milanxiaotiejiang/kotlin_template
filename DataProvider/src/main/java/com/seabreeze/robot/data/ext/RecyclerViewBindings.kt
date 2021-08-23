package com.seabreeze.robot.data.ext

import android.graphics.Rect
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

@BindingAdapter("itemDecorationSpacing")
fun RecyclerView.setItemDecorationSpacing(spacingPx: Float) {
    addItemDecoration(
        RecyclerViewItemDecoration(spacingPx.toInt())
    )
}

class RecyclerViewItemDecoration(
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val spacingPx: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> configSpacingForGridLayoutManager(
                outRect = outRect,
                layoutManager = layoutManager,
                position = parent.getChildViewHolder(view).adapterPosition,
                itemCount = state.itemCount
            )
            is LinearLayoutManager -> configSpacingForLinearLayoutManager(
                outRect = outRect,
                layoutManager = layoutManager,
                position = parent.getChildViewHolder(view).adapterPosition,
                itemCount = state.itemCount
            )
        }
    }

    private fun configSpacingForGridLayoutManager(
        outRect: Rect,
        layoutManager: GridLayoutManager,
        position: Int,
        itemCount: Int
    ) {
        val cols = layoutManager.spanCount
        val rows = ceil(itemCount / cols.toDouble()).toInt()

        outRect.top = spacingPx
        outRect.left = spacingPx
        outRect.right = if (position % cols == cols - 1) spacingPx else 0
        outRect.bottom = if (position / cols == rows - 1) spacingPx else 0
    }

    private fun configSpacingForLinearLayoutManager(
        outRect: Rect,
        layoutManager: LinearLayoutManager,
        position: Int,
        itemCount: Int
    ) {
        outRect.top = spacingPx
        outRect.left = spacingPx
        if (layoutManager.canScrollHorizontally()) {
            outRect.right = if (position == itemCount - 1) spacingPx else 0
            outRect.bottom = spacingPx
        } else if (layoutManager.canScrollVertically()) {
            outRect.right = spacingPx
            outRect.bottom = if (position == itemCount - 1) spacingPx else 0
        }
    }
}


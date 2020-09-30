package com.example.movies.movie_app.recyler_view

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import kotlin.math.abs


class MovieLayoutManager(
    private val context: Context?,
    orientation: Int,
    reverseLayout: Boolean
) :
    LinearLayoutManager(context, orientation, reverseLayout) {

    private val mShrinkAmount = 0.25f
    private val mShrinkDistance = 0.8f

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)


        val orientation = orientation
        if (orientation == HORIZONTAL) {
            shrinkByHorizontalScroll()
        } else {
            shrinkByVerticalScroll()
        }
    }

    private fun shrinkByHorizontalScroll(){
        val midpoint = width / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount
        for (i in 0 until childCount) {
            val child: View? = getChildAt(i)
            child?.let { child_ ->
                val childMidpoint =
                    (getDecoratedRight(child_) + getDecoratedLeft(child_)) / 2f
                val d =
                    d1.coerceAtMost(abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child_.scaleX = scale
                child_.scaleY = scale
            } ?: continue

        }
    }

    private fun shrinkByVerticalScroll(){
        val midpoint = height / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childMidpoint =
                (getDecoratedBottom(child!!) + getDecoratedTop(child)) / 2f
            val d =
                Math.min(d1, Math.abs(midpoint - childMidpoint))
            val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        val orientation = orientation
        return if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            shrinkByVerticalScroll()
            scrolled
        } else {
            0
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            shrinkByHorizontalScroll()
            scrolled
        } else {
            0
        }
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        scrollToCenter(position)
    }

    fun scrollToCenter(position: Int) {
        val smoothScroller: SmoothScroller =
            CenterSmoothScroller(
                context
            )

        smoothScroller.targetPosition = position

        startSmoothScroll(smoothScroller)
    }

    class CenterSmoothScroller internal constructor(context: Context?) :
        LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }
    }

}
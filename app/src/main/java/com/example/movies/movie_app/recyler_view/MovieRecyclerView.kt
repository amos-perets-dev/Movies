package com.example.movies.movie_app.recyler_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.movies.movie_app.MovieApplication
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("ViewConstructor")
class MovieRecyclerView(context: Context, attributeSet: AttributeSet) :
    RecyclerView(context, attributeSet) {

    init {
        setHasFixedSize(true)
        setItemViewCacheSize(40)
        isDrawingCacheEnabled = true
        drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        isNestedScrollingEnabled = false
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    companion object {

        abstract class Adapter<Model>(private var items: List<Model>) :
            RecyclerView.Adapter<ViewHolder<Model>>() {
            constructor() : this(Collections.emptyList())

            private val setVH = HashSet<ViewHolder<Model>>()

            private var screenWidth: Int? = 0
            private var screenHeight: Int? = 0
            private var itemWidth: Int = 0
            private var itemOffsetWidth: Int = 0
            private var itemOffsetHeight: Int = 0
            private val context =
                MovieApplication.context

            init {
                val displayMetrics = context?.resources?.displayMetrics
                screenWidth = displayMetrics?.widthPixels
                screenHeight = displayMetrics?.heightPixels

                if (screenWidth != null) {
                    itemWidth = ((screenWidth!! / 1.4f).toInt())
                    itemOffsetWidth = (screenWidth!! * 0.2).toInt()
                    itemOffsetHeight = (screenHeight!! * 0.2).toInt()
                }

            }

            private lateinit var recyclerView: RecyclerView

            fun updateList(data: List<Model>) {
                this.items = ArrayList(data)
                notifyDataSetChanged()

            }

            fun getDataList() : List<Model>{
                return this.items
            }

            override fun onBindViewHolder(holder: ViewHolder<Model>, position: Int) {

                val model = getItemByPos(position) ?: getItem(position)

                holder.setDataModel(model)

                if (isNeedChangeItemSize()) {
                    changeItemSize(holder)
                }

            }

            protected open fun isNeedChangeItemSize() = false

            protected open fun isNeedChangeItemWidth() = false

            protected open fun isNeedChangeItemHeight() = false

            protected open fun isFirstItemNeedChangeItemSize() = false

            protected open fun getScreenWidth() =
                context?.resources?.displayMetrics?.widthPixels ?: 0

            protected open fun getScreenHeight() =
                context?.resources?.displayMetrics?.heightPixels ?: 0

            protected open fun getFactorHeight() = 1.8f

            protected open fun getFactorWidth() = 1.65f

            private val currentPosition = PublishSubject.create<Int>()

            private fun changeItemSize(holder: ViewHolder<Model>) {

                val lp = holder.itemView.layoutParams

                if (isNeedChangeItemHeight()) {
                    val screenHeight = getScreenHeight()
                    lp.height = ((screenHeight / getFactorHeight()).toInt())
                }

                if (isNeedChangeItemWidth()) {
                    val screenWidth = getScreenWidth()
                    val itemWidth = (screenWidth / getFactorWidth()).toInt()
                    lp.width = itemWidth
                }
                holder.itemView.layoutParams = lp
            }

            private fun getItemByPos(position: Int): Model {
                return items[position]
            }

            open fun dispose() {

                setVH.forEach { it.destroy() }
            }

            abstract fun getItem(position: Int): Model

            override fun getItemCount(): Int = items.size

            override fun onViewRecycled(holder: ViewHolder<Model>) {
                super.onViewRecycled(holder)
                holder.destroy()
            }

            @CallSuper
            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                this.recyclerView = recyclerView

                val layoutManager = recyclerView.layoutManager
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        currentPosition.onNext(getCurrentPosition(layoutManager))
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == SCROLL_STATE_IDLE) {
                            val position = getCurrentPosition(layoutManager)
                            scrollToPositionWithOffset(
                                LinearLayoutManager.HORIZONTAL,
                                position
                            )
                        }
                    }
                })
            }

            fun getCurrentPosition(layoutManager: LayoutManager?): Int {
                val findFirstVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                val findFirstCompletelyVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                return if (findFirstCompletelyVisibleItemPosition == -1) {
                    findFirstVisibleItemPosition
                } else {
                    findFirstCompletelyVisibleItemPosition

                }
            }

            protected open fun addViewHolder(viewHolder: ViewHolder<Model>): ViewHolder<Model> {
                setVH.add(viewHolder)
                return viewHolder
            }

            fun scrollToPositionWithOffset(direction: Int, position: Int = 1) {
                val layoutManager = recyclerView.layoutManager

                val offset = if (direction == LinearLayoutManager.HORIZONTAL) {
                    itemOffsetWidth
                } else {
                    itemOffsetHeight
                }
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, offset)
            }

            fun getCurrentScrollPosition(): Observable<Int> = currentPosition.hide()

        }

        abstract class ViewHolder<Model>(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            constructor(parent: ViewGroup, layoutId: Int) : this(
                getLayout(
                    parent,
                    layoutId
                )
            )

            init {
                itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            }

            protected val compositeDisposable = CompositeDisposable()

            private var currentModel: Model? = null

            open fun bindData(model: Model) {}

            open fun unBindData(model: Model) {}


            fun setDataModel(model: Model) {

                if (currentModel != null) {
                    unBindData(currentModel!!)
                }

                currentModel = model

                if (model != null) {
                    bindData(model)
                }
            }

            open fun destroy() {

            }

            protected fun getDataModel() = this.currentModel
        }

        fun getLayout(parent: ViewGroup, layoutId: Int): View =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)


    }


}
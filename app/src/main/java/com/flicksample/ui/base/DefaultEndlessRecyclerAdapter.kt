package com.flicksample.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flicksample.R
import com.flicksample.util.inflater
import java.lang.IllegalArgumentException

abstract class DefaultEndlessRecyclerAdapter<VH : RecyclerView.ViewHolder> :
    EndlessRecyclerAdapter<RecyclerView.ViewHolder>() {

    private val spanSizes = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(pos: Int): Int {
            return if (pos == getCount() && (isLoading || isError))
                spanCount
            else if (originalSpanLookup == null) 1 else originalSpanLookup!!.getSpanSize(pos)
        }
    }

    private var originalSpanLookup: GridLayoutManager.SpanSizeLookup? = null
    private var spanCount: Int = 0

    private var oldIsLoading: Boolean = false
    private var oldIsError: Boolean = false

    abstract fun getCount(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EXTRA_LOADING_TYPE -> LoadingViewHolder(parent)
            EXTRA_ERROR_TYPE -> ErrorViewHolder(parent, this)
            else -> throw IllegalArgumentException("Not recognized viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == getCount()) {
            if (isLoading) {
                return EXTRA_LOADING_TYPE
            } else if (isError) {
                return EXTRA_ERROR_TYPE
            }
        }

        val type = getViewType(position)

        require(type != EXTRA_LOADING_TYPE) { "Cannot use $EXTRA_LOADING_TYPE as view type" }
        require(type != EXTRA_ERROR_TYPE) { "Cannot use $EXTRA_ERROR_TYPE as view type" }

        return type
    }

    private fun getViewType(position: Int): Int {
        return EXTRA_ITEM_TYPE
    }


    override fun onLoadingStateChanged() {
        super.onLoadingStateChanged()

        if (oldIsLoading) {
            if (isError) {
                notifyItemChanged(getCount()) // Switching to error view
            } else if (!isLoading) {
                notifyItemRemoved(getCount()) // Loading view is removed
            }
        } else if (oldIsError) {
            if (isLoading) {
                notifyItemChanged(getCount()) // Switching to loading view
            } else if (!isError) {
                notifyItemRemoved(getCount()) // Error view is removed
            }
        } else {
            if (isLoading || isError) {
                notifyItemInserted(getCount()) // Showing loading or error view
            }
        }

        oldIsError = isError
        oldIsLoading = isLoading
    }

    override fun getItemCount(): Int {
        return getCount() + if (isLoading || isError) 1 else 0
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is GridLayoutManager) {
            val gridManager = recyclerView.layoutManager as GridLayoutManager?
            spanCount = gridManager!!.spanCount
            originalSpanLookup = gridManager.spanSizeLookup
            gridManager.spanSizeLookup = spanSizes
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (recyclerView.layoutManager is GridLayoutManager) {
            val gridManager = recyclerView.layoutManager as GridLayoutManager?
            gridManager!!.spanSizeLookup = originalSpanLookup
            originalSpanLookup = null
            spanCount = 1
        }
    }


    private class LoadingViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater(parent, R.layout.item_load_more, false))

    private class ErrorViewHolder(
        parent: ViewGroup,
        adapter: EndlessRecyclerAdapter<*>
    ) : RecyclerView.ViewHolder(inflater(parent, R.layout.item_error_loading, false)) {
        init {
            itemView.setOnClickListener { adapter.reloadNextItemsIfError() }
        }
    }

    companion object {
        private val EXTRA_LOADING_TYPE = Integer.MAX_VALUE
        val EXTRA_ITEM_TYPE = 0
        private val EXTRA_ERROR_TYPE = Integer.MAX_VALUE - 1
    }
}
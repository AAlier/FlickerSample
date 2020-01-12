package com.flicksample.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            loadNextItemsIfNeeded(recyclerView)
        }
    }

    internal var isLoading: Boolean = false
        private set
    internal var isError: Boolean = false
        private set

    private var callbacks: LoaderCallbacks? = null
    private var loadingOffset = 0

    fun setCallbacks(callbacks: LoaderCallbacks) {
        this.callbacks = callbacks
        loadNextItems()
    }

    fun setLoadingOffset(loadingOffset: Int) {
        this.loadingOffset = loadingOffset
    }

    private fun loadNextItems() {
        if (!isLoading && !isError && callbacks != null && callbacks!!.canLoadNextItems()) {
            isLoading = true
            onLoadingStateChanged()
            callbacks!!.loadNextItems()
        }
    }

    internal fun reloadNextItemsIfError() {
        if (isError) {
            isError = false
            onLoadingStateChanged()
            loadNextItems()
        }
    }

    fun onNextItemsLoaded() {
        if (isLoading) {
            isLoading = false
            isError = false
            onLoadingStateChanged()
        }
    }

    fun onNextItemsError() {
        if (isLoading) {
            isLoading = false
            isError = true
            onLoadingStateChanged()
        }
    }

    protected open fun onLoadingStateChanged() {
        // No-default-op
    }

    private fun loadNextItemsIfNeeded(recyclerView: RecyclerView) {
        if (!isLoading && !isError) {
            val lastVisibleChild = recyclerView.getChildAt(recyclerView.childCount - 1)
            val lastVisiblePos = recyclerView.getChildAdapterPosition(lastVisibleChild)
            val total = itemCount

            if (lastVisiblePos >= total - loadingOffset) {
                // We need to use runnable, since recycler view does not like when we are notifying
                // about changes during scroll callback.
                recyclerView.post { loadNextItems() }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(scrollListener)
        loadNextItemsIfNeeded(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(scrollListener)
    }

    interface LoaderCallbacks {
        fun canLoadNextItems(): Boolean

        fun loadNextItems()
    }
}

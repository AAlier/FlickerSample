package com.flicksample.util

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

abstract class SearchQueryListener(lifecycle: Lifecycle) : SearchView.OnQueryTextListener,
    LifecycleObserver {
    var debouncePeriod: Long = 300
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var searchJob: Job? = null

    init {
        lifecycle.addObserver(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = scope.launch {
            newText?.let {
                delay(debouncePeriod)
                onQueryChanged(newText)
            }
        }
        return false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        searchJob?.cancel()
    }

    abstract fun onQueryChanged(newText: String?);
}
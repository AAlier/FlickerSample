package com.flicksample.ui.main

import android.text.TextUtils
import com.flicksample.data.network.interactor.SearchPhotosInteractor
import com.flicksample.model.Photo
import com.flicksample.model.PhotoList
import com.flicksample.util.FlickrApi
import com.flicksample.util.FlickrApi.DEFAULT_QUERY_PARAM
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainPresenter(
    private val view: MainContract.View,
    private val interactor: SearchPhotosInteractor
) : MainContract.Presenter, CoroutineScope {
    private var job: Job = Job()
    private var search: Job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    private val photos = ArrayList<Photo>()
    private val pages = ArrayList<PhotoList>()
    private var searchActive: Boolean = false

    override fun getPhotos(count: Int) {
        launch(errorHandler) {
            val (result, hasNext) = getNextValues(count)
            withContext(Dispatchers.Main) {
                view.onSuccess(result, hasNext)
            }
        }
    }

    override fun query(query: String?) {
        view.showProgress()
        search.cancel()
        search = launch(errorHandler) {
            if (TextUtils.isEmpty(query)) {
                withContext(Dispatchers.Main) {
                    view.onSuccess(emptyList(), false)
                    view.hideProgress()
                }
                return@launch
            }

            val response = interactor.search(query!!)
            withContext(Dispatchers.Main) {
                view.onSuccess(response?.photos?.photo ?: emptyList(), false)
                view.hideProgress()
                searchActive = true
                view.setSearchActive(searchActive)
            }
        }
        search.start()
    }

    private suspend fun getNextValues(count: Int, query: String = DEFAULT_QUERY_PARAM) =
        withContext(Dispatchers.Default) {
            var hasNext = hasNext()
            while (photos.size < count && hasNext) {
                val response = interactor.search(query)
                pages.add(response!!.photos)
                photos.addAll(response.photos.photo)
                hasNext = hasNext()
            }

            val resultSize = if (photos.size >= count) count else photos.size

            val result = ArrayList(photos.subList(0, resultSize))
            if (!hasNext) {
                hasNext = photos.size > count
            }
            return@withContext Pair(result, hasNext)
        }

    private fun hasNext(): Boolean {
        return when {
            pages.isEmpty() -> true
            pages.size >= FlickrApi.MAX_PAGES -> false
            else -> {
                val page = pages[pages.size - 1]
                page.page * page.perPage < page.total
            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        view.onError()
    }

    override fun disableSearch() {
        search.cancel()
        searchActive = false
        view.setSearchActive(false)
        launch(Dispatchers.Main) {
            view.onSuccess(photos, hasNext())
            view.hideProgress()
        }
    }

    override fun isSearchActive(): Boolean = searchActive

    override fun onDestroy() {
        search.cancel()
        job.cancel()
    }
}
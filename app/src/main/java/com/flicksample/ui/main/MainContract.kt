package com.flicksample.ui.main

import com.flicksample.model.Photo

interface MainContract {

    interface View {
        fun onSuccess(
            result: List<Photo>,
            hasNext: Boolean
        )
        fun onError()
        fun setSearchActive(isActive: Boolean)
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter {
        fun getPhotos(count: Int)
        fun query(query: String?)
        fun isSearchActive(): Boolean
        fun onDestroy()
        fun disableSearch()
    }
}
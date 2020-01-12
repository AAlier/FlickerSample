package com.flicksample

import android.app.Application
import com.flicksample.data.SearchQueryCacheManager

class FlickrApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SearchQueryCacheManager.init(this)
    }
}
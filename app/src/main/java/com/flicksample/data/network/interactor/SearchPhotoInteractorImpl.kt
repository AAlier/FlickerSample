package com.flicksample.data.network.interactor

import com.flicksample.data.network.ForumService
import com.flicksample.model.SearchResult
import com.flicksample.util.FlickrApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPhotoInteractorImpl(private val service: ForumService) : SearchPhotosInteractor {

    override suspend fun search(query: String): SearchResult? = withContext(Dispatchers.IO) {
        return@withContext service.getPhotos(
            method = FlickrApi.METHOD,
            key = FlickrApi.API_KEY,
            extras = FlickrApi.EXTRAS,
            query = query,
            format = FlickrApi.FORMAT,
            set = "1",
            perPage = "${FlickrApi.PER_PAGE}"
        )
    }

}
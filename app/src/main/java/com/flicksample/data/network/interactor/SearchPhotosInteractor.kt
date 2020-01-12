package com.flicksample.data.network.interactor

import com.flicksample.model.SearchResult

interface SearchPhotosInteractor {

    suspend fun search(query: String): SearchResult?
}
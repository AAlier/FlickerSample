package com.flicksample.data.network

import com.flicksample.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ForumService {

    @GET("/services/rest/")
    suspend fun getPhotos(
        @Query("method") method: String,
        @Query("api_key") key: String,
        @Query("extras") extras: String,
        @Query("text") query: String,
        @Query("per_page") perPage: String,
        @Query("format") format: String,
        @Query("nojsoncallback") set: String
    ): SearchResult
}
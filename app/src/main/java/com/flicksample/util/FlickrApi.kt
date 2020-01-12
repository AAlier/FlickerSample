package com.flicksample.util

object FlickrApi {
    val API_KEY: String = "fd9171ed8012b017530fc0ae9ed0d284"
    val SECRET: String = "3bb354984ec3c6f8"
    // val API_KEY = "7f6035774a01a39f9056d6d7bde60002"

    val PER_PAGE = 30
    val MAX_PAGES = 5
    val EXTRAS = "url_s, url_m, url_l, owner_name"
    val DEFAULT_QUERY_PARAM = "landscape"
    val METHOD = "flickr.photos.search"
    val FORMAT = "json"
    val PAGE_SIZE = 30
}
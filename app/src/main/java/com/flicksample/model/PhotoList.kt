package com.flicksample.model

import com.google.gson.annotations.SerializedName

data class PhotoList(
    var page: Int = 0,
    var pages: Int = 0,
    @SerializedName("perpage")
    var perPage: Int = 0,
    var total: Int = 0,
    val photo: List<Photo>
)
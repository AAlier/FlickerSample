package com.flicksample.model

import com.google.gson.annotations.SerializedName

data class Photo(
    var id: String? = null,
    var title: String? = null,
    var ownername: String? = null,

    @SerializedName("url_t")
    var thumbnailUrl: String? = null,
    @SerializedName("height_t")
    var thumbnailHeight: Int? = null,
    @SerializedName("width_t")
    var thumbnailWidth: Int? = null,

    @SerializedName("url_s")
    var smallUrl: String? = null,
    @SerializedName("height_s")
    var smallHeight: Int? = null,
    @SerializedName("width_s")
    var smallWidth: Int? = null,

    @SerializedName("url_m")
    var mediumUrl: String? = null,
    @SerializedName("height_m")
    var mediumHeight: Int? = null,
    @SerializedName("width_m")
    var mediumWidth: Int? = null,

    @SerializedName("url_l")
    var largeUrl: String? = null,
    @SerializedName("height_l")
    var largeHeight: Int? = null,
    @SerializedName("width_l")
    var largeWidth: Int? = null
)
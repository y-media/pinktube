package com.example.spartube.data.model.searchmodel


import com.google.gson.annotations.SerializedName

data class ResponseSearch(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("nextPageToken")
    val nextPageToken: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("regionCode")
    val regionCode: String
)
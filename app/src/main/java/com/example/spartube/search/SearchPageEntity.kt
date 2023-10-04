package com.example.spartube.search

import com.example.spartube.home.BindingModel

data class SearchPageEntity(
    val title: String,
    val videoId: String,
    val description: String,
    val publishedAt: String,
    val thumbnails: String
) {
    fun toBindingModel() = BindingModel(
        videoId, title, thumbnails, null, "", publishedAt, description
    )
}
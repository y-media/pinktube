package com.example.spartube.shorts.adapter

import com.example.spartube.data.model.commentmodel.Replies
import com.example.spartube.data.model.commentmodel.SnippetX
import com.example.spartube.util.Converter

data class BindingModel(
    val linkId: String,
    val channelId: String?,
    val title: String?,
    val description: String?,
    val thumbnail: String?,
    var isLiked: Boolean
)

data class CommentBindingModel(
    val userImageUrl: String?,
    val userName: String?,
    val publishedAt: String?,
    val description: String?,
    val likeCount: Int?,
    val viewType: Int = 1
)

data class CommentSetBindingModel(
    val topLevelCommentType: SnippetX,
    val repliesFromTop: Replies?,
    var viewType: Int
) {
    fun toCommentBindingModel(): CommentBindingModel {
        val snippet = topLevelCommentType.topLevelComment.snippet
        return CommentBindingModel(
            userImageUrl = snippet.authorProfileImageUrl,
            userName = snippet.authorDisplayName,
            publishedAt = Converter.getDateFromTimestampWithFormat(snippet.publishedAt),
            description = snippet.textDisplay,
            likeCount = snippet.likeCount,
            viewType = 0
        )
    }
}
package com.example.spartube.comment

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spartube.databinding.ShortsPageCommentItemBinding
import com.example.spartube.shorts.adapter.CommentSetBindingModel

class CommentsViewHolder(
    private val binding: ShortsPageCommentItemBinding,
    private val onClickReply: (CommentSetBindingModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: CommentSetBindingModel) = with(binding) {
        val commentModel = model.toCommentBindingModel()
        val replySize = model.repliesFromTop?.comments?.size
        commentUserTextView.text = commentModel.userName
        commentDescriptionTextView.text = commentModel.description
        commentTimeTextView.text = commentModel.publishedAt
        commentThumbsUpTextView.text = commentModel.likeCount.toString()
        commentProfileImageView.load(commentModel.userImageUrl)
        if (replySize != 0) {
            commentReplyTextView.run {
                if (replySize == null) {
                    isVisible = false
                } else {
                    isVisible = true
                    "답글 ${replySize}개".also { text = it }
                }
            }
        }
        commentReplyTextView.setOnClickListener {
            onClickReply(model)
        }
    }
}
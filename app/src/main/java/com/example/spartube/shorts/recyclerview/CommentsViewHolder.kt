package com.example.spartube.shorts.recyclerview

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.spartube.databinding.ShortsPageCommentItemBinding

import com.example.spartube.shorts.CommentBindingModel

class CommentsViewHolder(
    private val binding: ShortsPageCommentItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: CommentBindingModel) = with(binding) {
        commentUserTextView.text = model.userName
        commentDescriptionTextView.text = model.description
        commentTimeTextView.text = model.publishedAt
        commentThumbsUpTextView.text = model.likeCount.toString()
        commentProfileImageView.load(model.userImageUrl)
    }
}
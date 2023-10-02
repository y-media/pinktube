package com.example.spartube.shorts.recyclerviewutil.reply

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spartube.databinding.ShortsPageReplyitemBinding
import com.example.spartube.shorts.recyclerviewutil.CommentBindingModel

class ReplyViewHolder(
    private val binding: ShortsPageReplyitemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: CommentBindingModel) = with(binding) {
        commentUserTextView.text = model.userName
        commentDescriptionTextView.text = model.description
        commentTimeTextView.text = model.publishedAt
        commentThumbsUpTextView.text = model.likeCount.toString()
        commentProfileImageView.load(model.userImageUrl)
    }
}
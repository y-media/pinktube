package com.example.spartube.shorts.recyclerviewutil.reply

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spartube.databinding.ShortsPageCommentItemBinding
import com.example.spartube.shorts.recyclerviewutil.CommentBindingModel

class MainCommentViewHolder(
    private val binding: ShortsPageCommentItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ResourceAsColor")
    fun bind(model: CommentBindingModel) = with(binding) {
        binding.root.setBackgroundColor(Color.parseColor("#ebebeb"))
        commentUserTextView.text = model.userName
        commentDescriptionTextView.text = model.description
        commentTimeTextView.text = model.publishedAt
        commentThumbsUpTextView.text = model.likeCount.toString()
        commentProfileImageView.load(model.userImageUrl)
    }
}
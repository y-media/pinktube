package com.example.spartube.shorts.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ShortsPageCommentItemBinding
import com.example.spartube.shorts.CommentBindingModel

class CommentsAdapter : RecyclerView.Adapter<CommentsViewHolder>() {

    private val list = arrayListOf<CommentBindingModel>()
    fun addItems(items: List<CommentBindingModel>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            ShortsPageCommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
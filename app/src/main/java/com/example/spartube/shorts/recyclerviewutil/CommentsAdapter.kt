package com.example.spartube.shorts.recyclerviewutil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ShortsPageCommentItemBinding

class CommentsAdapter(
    private val onClickReply: (CommentSetBindingModel) -> Unit
) : RecyclerView.Adapter<CommentsViewHolder>() {

    private val list = arrayListOf<CommentSetBindingModel>()
    fun addItems(items: List<CommentSetBindingModel>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun addMoreItems(items: List<CommentSetBindingModel>) {
        list.addAll(items)
        notifyItemInserted(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            ShortsPageCommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClickReply
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}
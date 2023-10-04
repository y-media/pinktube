package com.example.spartube.comment.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ShortsPageCommentItemBinding
import com.example.spartube.databinding.ShortsPageReplyitemBinding
import com.example.spartube.comment.ViewType
import com.example.spartube.shorts.adapter.CommentBindingModel
import java.lang.RuntimeException

class ReplyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = arrayListOf<CommentBindingModel>()
    fun addItems(items: List<CommentBindingModel>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TOP.order -> {
                MainCommentViewHolder(
                    ShortsPageCommentItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ViewType.OTHER.order -> {
                ReplyViewHolder(
                    ShortsPageReplyitemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw RuntimeException("disable view type")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (item.viewType) {
            ViewType.TOP.order -> (holder as MainCommentViewHolder).bind(list[position])
            ViewType.OTHER.order -> (holder as ReplyViewHolder).bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
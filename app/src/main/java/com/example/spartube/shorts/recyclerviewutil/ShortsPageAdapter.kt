package com.example.spartube.shorts.recyclerviewutil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ShortsPageItemBinding

@UnstableApi
class ShortsPageAdapter(
    private val context: Context,
    private val onClickItem: (Int, BindingModel, View, ExoPlayer, Boolean) -> Unit,
    private val onClickShareView: (BindingModel) -> Unit,
    private val onClickLiked: (BindingModel, Boolean) -> Unit,
    private val onClickComment: (BindingModel) -> Unit
) : RecyclerView.Adapter<ShortsViewHolder>() {
    private val list = arrayListOf<BindingModel>()
    fun addItems(items: List<BindingModel>) {
//        list.clear()
        list.addAll(items)
        notifyItemInserted(items.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsViewHolder {
        return ShortsViewHolder(
            ShortsPageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context,
            onClickItem,
            onClickShareView,
            onClickLiked,
            onClickComment
        )
    }

    override fun getItemCount(): Int {
//        return Int.MAX_VALUE
        return list.size
    }

    override fun onBindViewHolder(holder: ShortsViewHolder, position: Int) {
//        val actualPosition = position % list.size
//        holder.bind(list[actualPosition])
        holder.bind(list[position])
    }
}
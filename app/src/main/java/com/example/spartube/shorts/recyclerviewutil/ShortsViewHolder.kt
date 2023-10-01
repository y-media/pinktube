package com.example.spartube.shorts.recyclerviewutil

import android.content.Context
import android.widget.ProgressBar
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ShortsPageItemBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@UnstableApi
class ShortsViewHolder(
    private val binding: ShortsPageItemBinding,
    private val context: Context,
    private val onClickShareView: (BindingModel) -> Unit,
    private val onClickLiked: (BindingModel, Boolean) -> Unit,
    private val onClickComment: (BindingModel) -> Unit,
    private val startShortsVideo: (BindingModel, YouTubePlayerView, ProgressBar) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: BindingModel) = with(binding) {
        shortsPageIvShare.setOnClickListener {
            onClickShareView(model)
        }
        shortsPageCheckboxLike.setOnCheckedChangeListener { _, isLiked ->
            onClickLiked(model, isLiked)
        }
        shortsPageIvComment.setOnClickListener {
            onClickComment(model)
        }
        shortsTitleView.text = model.title
        "@${model.channelId}".also { shortsChannelIdTextView.text = it }
        startShortsVideo(model, shortsPageVideoView, binding.progressBar)
    }
}
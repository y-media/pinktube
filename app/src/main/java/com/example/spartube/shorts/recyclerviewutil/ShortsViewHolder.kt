package com.example.spartube.shorts.recyclerviewutil

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.R
import com.example.spartube.databinding.ShortsPageItemBinding
import com.example.spartube.shorts.util.CustomPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@UnstableApi
class ShortsViewHolder(
    private val binding: ShortsPageItemBinding,
    private val context: Context,
    private val onClickShareView: (BindingModel) -> Unit,
    private val onClickLiked: (BindingModel, Boolean) -> Unit,
    private val onClickComment: (BindingModel) -> Unit,
    private val startShortsVideo: (BindingModel, YouTubePlayerView) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        val tracker = YouTubePlayerTracker()
        var duration: Float?
        val customUi =
            binding.shortsPageVideoView.inflateCustomPlayerUi(R.layout.shorts_custom_view)
        val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val customPlayerUiController = CustomPlayerUiController(customUi)
                youTubePlayer.run {
                    addListener(tracker)
                    addListener(customPlayerUiController)
                }
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                Handler(Looper.getMainLooper()).postDelayed({
                    duration = tracker.videoDuration
                    duration?.let {
                        updateProgressBar(it, binding.progressBar, tracker)
                    }
                }, 500)
            }
        }
        val iFramePlayerOptions = IFramePlayerOptions.Builder().apply {
            controls(0)
            rel(0)
            ivLoadPolicy(3)
            ccLoadPolicy(1)
            fullscreen(0)
        }.build()
        binding.shortsPageVideoView.initialize(youtubePlayerListener, false, iFramePlayerOptions)
    }

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
        startShortsVideo(model, shortsPageVideoView)
    }

    private fun updateProgressBar(
        duration: Float,
        progressBar: ProgressBar,
        player: YouTubePlayerTracker
    ) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentPosition = player.currentSecond
                progressBar.progress = ((currentPosition * 100) / duration.toInt()).toInt()
                handler.postDelayed(this, 500)
            }
        }
        handler.postDelayed(runnable, 0)
    }
}
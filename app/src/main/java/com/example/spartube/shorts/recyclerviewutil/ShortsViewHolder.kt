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

@UnstableApi
class ShortsViewHolder(
    private val binding: ShortsPageItemBinding,
    private val context: Context,
    private val onClickItem: (YouTubePlayer) -> Unit,
    private val onClickShareView: (BindingModel) -> Unit,
    private val onClickLiked: (BindingModel, Boolean) -> Unit,
    private val onClickComment: (BindingModel) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private var duration: Float? = 0f
    private lateinit var player: YouTubePlayer
    fun bind(model: BindingModel) = with(binding) {
        val tracker = YouTubePlayerTracker()
        val customUi = shortsPageVideoView.inflateCustomPlayerUi(R.layout.shorts_custom_view)
        val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val customPlayerUiController = CustomPlayerUiController(customUi)
                youTubePlayer.run {
                    player = this
                    addListener(tracker)
                    addListener(customPlayerUiController)
                    loadVideo("t1Jq8pGZ4gU", 0f)
//                    loadVideo(model.linkId", 0f)
                    play()
                }
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                Handler(Looper.getMainLooper()).postDelayed({
                    duration = tracker.videoDuration
                    duration?.let {
                        updateProgressBar(it, progressBar, tracker)
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
        shortsPageVideoView.initialize(youtubePlayerListener, false, iFramePlayerOptions)
        shortsPageVideoView.run {
            setOnClickListener { view ->
                onClickItem(player)
            }
        }
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
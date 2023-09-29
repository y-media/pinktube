package com.example.spartube.shorts

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.R
import com.example.spartube.databinding.ShortsPageItemBinding

@UnstableApi
class ShortsViewHolder(
    private val binding: ShortsPageItemBinding,
    private val context: Context,
    private val onClickItem: (Int, BindingModel, View, ExoPlayer, Boolean) -> Unit,
    private val onClickShareView: (BindingModel) -> Unit,
    private val onClickLiked: (BindingModel, Boolean) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private var duration: Long? = 0L
    fun bind(model: BindingModel) = with(binding) {
        val player = ExoPlayer.Builder(context).build()
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaItem =
            MediaItem.fromUri(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.tmp))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        shortsPageVideoView.player = player
        player.addListener(object : Player.Listener {
            // 플레이 여부가 바뀔 때 마다 실행되는 콜백  isPlaying, true - 실행  false - 멈춤
            override fun onIsPlayingChanged(isPlaying: Boolean): Unit = with(binding) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    shortsControlIconImageView.apply {
                        setImageResource(R.drawable.ic_play)
                        animate().alpha(0.0f).duration = 500
                    }
                } else {
                    shortsControlIconImageView.apply {
                        setImageResource(R.drawable.ic_pause)
                        animate().alpha(1.0f).duration = 500
                    }
                }
            }
        })
        shortsTitleView.text = model.title
        "@${model.channelId}".also { shortsChannelIdTextView.text = it }
        shortsPageVideoView.run {
            player.run {
                setMediaSource(mediaSource)
                playWhenReady = true
                prepare()
            }
            setOnClickListener { view ->
                onClickItem(
                    adapterPosition, model, view, player, player.isPlaying
                )
            }
        }
        shortsPageIvShare.setOnClickListener {
            onClickShareView(model)
        }
        shortsPageCheckboxLike.setOnCheckedChangeListener { _, isLiked ->
            onClickLiked(model, isLiked)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            duration = player.contentDuration
            duration?.let {
                updateProgressBar(it, progressBar, player)
            }
        }, 600)
    }

    private fun updateProgressBar(duration: Long, progressBar: ProgressBar, player: ExoPlayer) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentPosition = player.currentPosition
                progressBar.progress = ((currentPosition * 100) / duration.toInt()).toInt()
                handler.postDelayed(this, 500)
            }
        }
        handler.postDelayed(runnable, 0)
    }

}
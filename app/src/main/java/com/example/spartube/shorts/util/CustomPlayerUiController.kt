package com.example.spartube.shorts.util

import android.view.View
import android.widget.ImageView
import com.example.spartube.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class CustomPlayerUiController(
    customPlayerUi: View,
) : AbstractYouTubePlayerListener() {
    private var playControlImageView: ImageView? = null

    init {
        initViews(customPlayerUi)
    }

    private fun initViews(view: View) {
        playControlImageView = view.findViewById(R.id.shorts_control_icon_image_view)
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerState) {
        if (state == PlayerState.PLAYING || state == PlayerState.VIDEO_CUED) {
            playControlImageView?.apply {
                setImageResource(R.drawable.ic_play)
                animate().alpha(0.0f).duration = 500
            }
        } else if (state == PlayerState.BUFFERING || state == PlayerState.PAUSED) {
            playControlImageView?.apply {
                setImageResource(R.drawable.ic_pause)
                animate().alpha(1.0f).duration = 500
            }
        }
    }
}
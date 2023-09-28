package com.example.spartube.shorts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentShortsPageBinding
import com.example.spartube.shorts.util.PreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@UnstableApi
class ShortsPageFragment : Fragment() {
    private var _binding: FragmentShortsPageBinding? = null
    private val binding: FragmentShortsPageBinding
        get() = _binding!!
    private val shortsList = arrayListOf<BindingModel>()
    private val shortPageRecyclerAdapter by lazy {
        ShortsPageAdapter(
            context = requireActivity(),
            onClickItem = { position, model, view, player, isPlaying ->
                this.player = player
                controlVideo(position, model, view, player, isPlaying)
            }
        )
    }
    private var nextPageToken: String? = null
    private var player: ExoPlayer? = null
    private val endScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 스크롤이 최하단일 떄
                if (!binding.shortsPageRecyclerView.canScrollVertically(1)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        getShorts(nextPageToken)
                        requireActivity().runOnUiThread {
                            shortPageRecyclerAdapter.addItems(shortsList)
                        }
                    }
                }
            }
        }
    }
    private lateinit var prefs: PreferenceUtils

    companion object {
        const val BASE_VIDEO_URL = "https://www.youtube.com/watch?v="
        fun newInstance() = ShortsPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortsPageBinding.inflate(layoutInflater)
        prefs = PreferenceUtils(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        shortsPageRecyclerView.run {
            adapter = shortPageRecyclerAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            addOnScrollListener(endScrollListener)
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(shortsPageRecyclerView)
        fetchItems()
    }

    private fun fetchItems() {
        runCatching {
            CoroutineScope(Dispatchers.IO).launch {
                getShorts(null)
                initRecyclerView()
            }
        }.onFailure {
            Toast.makeText(requireActivity(), "로딩 실패", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun getShorts(pageToken: String?) {
        val responseShorts = RetrofitModule.getShortsVideos(pageToken)
        responseShorts.body()?.let { shorts ->
            nextPageToken = shorts.nextPageToken
            shorts.items.filter {
                filteringOverOneMinutes(it.contentDetails.duration).not()
            }.forEach { item ->
                shortsList.add(
                    BindingModel(
                        linkId = item.id,
                        channelId = item.snippet.channelId,
                        title = item.snippet.title,
                        replyCount = item.statistics.commentCount,
                        duration = item.contentDetails.duration
                    )
                )
            }
        }
    }

    private fun initRecyclerView() = with(binding) {
        if (isAdded) {
            requireActivity().runOnUiThread {
                shortPageRecyclerAdapter.addItems(shortsList)
            }
        }
    }

    private fun controlVideo(
        position: Int,
        model: BindingModel,
        view: View,
        player: ExoPlayer,
        isPlaying: Boolean
    ) = with(binding) {
        println(BASE_VIDEO_URL + model.linkId)
        if (isPlaying) {
            println("pause")
            pauseVideo(player)
        } else {
            println("play")
            playVideo(player)
        }
    }

    private fun playVideo(player: ExoPlayer) {
        println(prefs.time)
        if (prefs.time != null && prefs.time!! > 0L) {
            player.seekTo(prefs.time!!)
        }
        player.prepare()
        player.play()
    }

    private fun pauseVideo(player: ExoPlayer) {
        player.stop()
        prefs.time = player.currentPosition
    }


    private fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun filteringOverOneMinutes(duration: String): Boolean = duration.contains("M")

    override fun onDestroyView() {
        releasePlayer()
        _binding = null
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
    }
}

data class BindingModel(
    val linkId: String,
    val channelId: String?,
    val title: String?,
    val replyCount: String?,
    val duration: String?,
)
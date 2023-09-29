package com.example.spartube.shorts

import android.content.Intent
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
import com.example.spartube.db.AppDatabase
import com.example.spartube.db.MyPageEntity
import com.example.spartube.shorts.recyclerviewutil.BindingModel
import com.example.spartube.shorts.recyclerviewutil.CommentSetBindingModel
import com.example.spartube.shorts.recyclerviewutil.ShortsPageAdapter
import com.example.spartube.shorts.util.PreferenceUtils
import com.example.spartube.shorts.util.SnapPagerScrollListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@UnstableApi
class ShortsPageFragment : Fragment() {
    private var _binding: FragmentShortsPageBinding? = null
    private val binding: FragmentShortsPageBinding
        get() = _binding!!
    private val shortsList = arrayListOf<BindingModel>()
    private val commentsSetList = arrayListOf<CommentSetBindingModel>()
    private val shortPageRecyclerAdapter by lazy {
        ShortsPageAdapter(
            context = requireActivity(),
            onClickItem = { position, model, view, player, isPlaying ->
                this.player = player
                controlVideo(position, model, view, player, isPlaying)
            },
            onClickShareView = { model ->
                shareYoutubeInfo(model)
            },
            onClickLiked = { model, isLiked ->
                setIsLikedToDB(model, isLiked)
            },
            onClickComment = { model ->
                showCommentsWithBottomSheet(model)
            }
        )
    }

    private var nextPageToken: String? = null
    private var player: ExoPlayer? = null
    private val snapHelper = PagerSnapHelper()
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
    private val snapScrollListener by lazy {
        SnapPagerScrollListener(
            snapHelper,
            SnapPagerScrollListener.ON_SCROLL,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    player?.pause()
                }
            }
        )
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
            addOnScrollListener(snapScrollListener)
        }
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
                        linkId = item.id,  //
                        channelId = item.snippet.channelId, //
                        title = item.snippet.title, //
                        description = item.snippet.description, //
                        thumbnail = item.snippet.thumbnails.default.url, //
                        replyCount = item.statistics.commentCount, // 삭제
                        duration = item.contentDetails.duration // 삭제
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


    private fun shareYoutubeInfo(model: BindingModel) {
        val youtubeInfo = "${model.title} \n\n $BASE_VIDEO_URL${model.linkId}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, youtubeInfo)
            type = "text/plain"
        }
        val sharingIntent = Intent.createChooser(intent, "공유하기")
        startActivity(sharingIntent)
    }


    private fun setIsLikedToDB(model: BindingModel, liked: Boolean) =
        CoroutineScope(Dispatchers.IO).launch {
            val entity = MyPageEntity(
                thumbnailUrl = model.thumbnail,
                title = model.title,
                description = model.description
            )
            val database = AppDatabase.getDatabase(requireActivity())
            when (liked) {
                true -> database.myPageDao().insertVideo(entity)
                false -> database.myPageDao().deleteVideo(entity)
            }
//            database.close() //?
        }

    private fun showCommentsWithBottomSheet(model: BindingModel) = with(binding) {
        val bottomSheet = BottomSheetCommentFragment.newInstance(requireActivity())
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment.TAG)
        runCatching {
            getComments(model.linkId, bottomSheet)
        }.onFailure {
            Toast.makeText(requireActivity(), "로딩 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getComments(linkId: String, bottomSheetFragment: BottomSheetCommentFragment) {
        CoroutineScope(Dispatchers.IO).launch {
            commentsSetList.clear()
            val responseComments = RetrofitModule.getCommentsOfShorts(linkId)
            responseComments.body()?.let { comments ->
                comments.items.forEach { comment ->
                    commentsSetList.add(
                        CommentSetBindingModel(
                            comment.snippet,
                            comment.replies,
                            ViewType.OTHER.order
                        )

                    )
                    commentsSetList.first().viewType = ViewType.TOP.order
                }
                requireActivity().runOnUiThread {
                    bottomSheetFragment.addComments(commentsSetList)
                }
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
            pauseVideo(player)
        } else {
            playVideo(player)
        }
    }

    private fun playVideo(player: ExoPlayer) {
        if (prefs.time != null && prefs.time!! > 0L) {
            player.seekTo(prefs.time!!)
        }
        player.prepare()
        player.play()
        println(player.contentDuration)
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
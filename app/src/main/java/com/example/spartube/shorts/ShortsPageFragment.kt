package com.example.spartube.shorts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.R
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentShortsPageBinding
import com.example.spartube.db.AppDatabase
import com.example.spartube.db.MyPageEntity
import com.example.spartube.shorts.recyclerviewutil.BindingModel
import com.example.spartube.shorts.recyclerviewutil.CommentSetBindingModel
import com.example.spartube.shorts.recyclerviewutil.ShortsPageAdapter
import com.example.spartube.shorts.util.SnapPagerScrollListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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
            onClickShareView = { model ->
                shareYoutubeInfo(model)
            },
            onClickLiked = { model, isLiked ->
                setIsLikedToDB(model, isLiked)
            },
            onClickComment = { model ->
                showCommentsWithBottomSheet(model)
            },
            startShortsVideo = { model, youtubePlayerView ->
                startShortsVideo(model, youtubePlayerView)
            }
        )
    }

    //    private var firstChannelNextPageToken: String? = null
//    private var secondChannelNextPageToken: String? = null
//    private var thirdChannelNextPageToken: String? = null
    private val shortsNextPageTokenList = arrayListOf<String?>(null, null, null)
    private var player: YouTubePlayer? = null
    private val snapHelper = PagerSnapHelper()
    private val endScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 스크롤이 최하단일 떄
                if (!binding.shortsPageRecyclerView.canScrollVertically(1)) {
                    CoroutineScope(Dispatchers.IO).launch {
//                        getShorts(nextPageToken)
//                        requireActivity().runOnUiThread {
//                            shortPageRecyclerAdapter.addItems(shortsList)
//                        }
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
    private var youtubePlayerView: YouTubePlayerView? = null

    companion object {
        const val BASE_VIDEO_URL = "https://www.youtube.com/watch?v="
        fun newInstance() = ShortsPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortsPageBinding.inflate(layoutInflater)
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
                getShorts()
                initRecyclerView()
            }
        }.onFailure {
            Toast.makeText(requireActivity(), "로딩 실패", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun getShorts() {
        val channelList = listOf(
            "UCYH0isveXrujjCH4Z2F4p4g",
            "UC8rqHW_RVriJ5TzQLjlWo_Q",
            "UCuuF5I3mo6rlhHLURrIDB9Q"
        )
        channelList.forEachIndexed { index, channelId ->
            val responseShorts =
                RetrofitModule.getShortsVideos(shortsNextPageTokenList[index], channelId)
            responseShorts.body()?.let { shortsData ->
                shortsNextPageTokenList[index] = shortsData.nextPageToken
                shortsData.items.forEach { item ->
                    shortsList.add(
                        BindingModel(
                            linkId = item.id.videoId,
                            channelId = item.snippet.channelId,
                            title = item.snippet.title,
                            description = item.snippet.description,
                            thumbnail = item.snippet.thumbnails.default.url,
                        )
                    )
                }
            }
        }
        shortsList.shuffle() // 데이터 섞기
    }

    private fun initRecyclerView() {
        if (isAdded) {
            requireActivity().runOnUiThread {
                shortPageRecyclerAdapter.addItems(shortsList)
                youtubePlayerView =
                    view?.findViewById(R.id.shorts_page_video_view)
                youtubePlayerView?.let {
                    lifecycle.addObserver(it)
                }
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
        val bottomSheet = BottomSheetCommentFragment.newInstance().apply {
            arguments = Bundle().apply {
                putString("videoId", model.linkId)
            }
        }
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment.TAG).runCatching {
            getComments(model.linkId, bottomSheet)
        }.onFailure {
            Toast.makeText(requireActivity(), "로딩 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getComments(linkId: String, bottomSheetFragment: BottomSheetCommentFragment) {
        CoroutineScope(Dispatchers.IO).launch {
            commentsSetList.clear()
            val responseComments = RetrofitModule.getCommentsOfShorts(linkId, null)
            responseComments.body()?.let { comments ->
                comments.items.forEach { comment ->
                    commentsSetList.add(
                        CommentSetBindingModel(
                            comment.snippet,
                            comment.replies,
                            ViewType.OTHER.order,
                        )
                    )
                    commentsSetList.first().viewType = ViewType.TOP.order
                }
                requireActivity().runOnUiThread {
                    bottomSheetFragment.addComments(commentsSetList, comments.nextPageToken)
                }
            }
        }
    }

    private fun startShortsVideo(
        model: BindingModel,
        youtubePlayerView: YouTubePlayerView,
    ) {
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.run {
                    loadVideo(model.linkId, 0f)
                    play()
                }
            }
        })
    }

    override fun onDestroyView() {
        youtubePlayerView?.release()
        _binding = null
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }
}
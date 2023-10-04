package com.example.spartube.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.spartube.R
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentDetailPageBinding
import com.example.spartube.comment.BottomSheetCommentFragment
import com.example.spartube.comment.ViewType
import com.example.spartube.shorts.adapter.CommentSetBindingModel
import com.example.spartube.db.AppDatabase
import com.example.spartube.db.MyPageEntity
import com.google.android.material.tabs.TabLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailPageFragment : Fragment() {
    private lateinit var binding: FragmentDetailPageBinding
    private var _binding: FragmentDetailPageBinding? = null
    private val commentsSetList = arrayListOf<CommentSetBindingModel>()


    companion object {

        fun newInstance() = DetailPageFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPageBinding.inflate(inflater, container, false)


        //뒤로가기
        binding.btnDetailBack.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val viewPager2 =

                requireActivity().findViewById<ViewPager2>(R.id.activity_main_viewpager)
            fragmentTransaction.setCustomAnimations(R.anim.slide_up_enter, R.anim.slide_down_exit)
                .remove(this).commit()  //디테일페이지 사라지기 애니메이션
            requireActivity().findViewById<TabLayout>(R.id.activity_main_tab).isVisible = true
            viewPager2.isVisible = true
            viewPager2.setCurrentItem(0, false)
        }
        requireActivity().findViewById<TabLayout>(R.id.activity_main_tab).isVisible = false

        initShare()
        return binding.root
    }


    private fun showCommentsWithBottomSheet(id: String?) = with(binding) {
        val bottomSheet = BottomSheetCommentFragment.newInstance().apply {
            arguments = Bundle().apply {
                putString("videoId", id)
            }
        }
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment.TAG).runCatching {
            getComments(id ?: "", bottomSheet)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("model_id")
        val title = arguments?.getString("model_title")
        val content = arguments?.getString("model_content")

        val thumbnail = arguments?.getString("model_url")
        val description = ""

        val youTubePlayerView: YouTubePlayerView =
            binding.pvDetailPlayer
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = id ?: ""
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.play()
            }
        })
        binding.tvDetailId.setText(id)
        binding.tvDetailTitle.setText(title)
        binding.tvDetailContent.setText(content)
        binding.ivDetailComment.setOnClickListener {
            showCommentsWithBottomSheet(id)
        }




        binding.ivDetailHeart.setOnClickListener {
//데이터 처리, 좋아요 구현
            val roomData = mutableListOf<MyPageEntity>()
            val newItem = MyPageEntity(
                thumbnailUrl = thumbnail,
                title = title,
                description = description
            )
            addDataToDatabase(newItem)
            roomData.add(newItem)
            Toast.makeText(requireContext(), "myPage에 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDataToDatabase(item: MyPageEntity) {
        val dao = AppDatabase.getDatabase(requireContext()).myPageDao()

        CoroutineScope(Dispatchers.IO).launch {
            dao.insertVideo(item)
        }


    }

    //intent.ACTION_SEND를 이용한 공유 기능
    private fun initShare() {
        binding.ivDetailShare.setOnClickListener {
            val id = arguments?.getString("model_id")

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=${id}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            activity?.startActivity(shareIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
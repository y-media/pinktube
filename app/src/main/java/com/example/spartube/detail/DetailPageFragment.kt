package com.example.spartube.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.spartube.R
import com.example.spartube.databinding.FragmentDetailPageBinding
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

    //    private lateinit var adapterHome: HomeAdapter
//    private lateinit var adapterCategory: CategoryAdapter
//    private lateinit var adapterChannel: CategoryChannelAdapter
    private var youtube: YouTubePlayerView? = null


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
            val detailPageFragment = DetailPageFragment
            val ViewPager2 =
                requireActivity().findViewById<ViewPager2>(R.id.activity_main_viewpager)
            requireActivity().findViewById<FragmentContainerView>(R.id.container_detail).isVisible =
                false
            requireActivity().findViewById<TabLayout>(R.id.activity_main_tab).isVisible = true
            ViewPager2.isVisible = true
            ViewPager2.setCurrentItem(0, false)
        }
        requireActivity().findViewById<TabLayout>(R.id.activity_main_tab).isVisible = false

        initShare()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("model_id")
        val title = arguments?.getString("model_title")

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
        println(id)
        println(title)

        binding.ivDetailHeart.setOnClickListener {
//데이터 처리, 좋아요 구현
            val roomData = mutableListOf<MyPageEntity>()
            val newItem = MyPageEntity(
                id = 1000,
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
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=GHXQnCGiirE")
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

data class DetailBindingModel(
    val id: String,
    val title: String?,
    val content: String?,

    )
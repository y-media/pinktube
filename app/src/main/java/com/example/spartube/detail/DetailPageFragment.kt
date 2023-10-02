package com.example.spartube.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.spartube.R
import com.example.spartube.databinding.FragmentDetailPageBinding
import com.google.android.material.tabs.TabLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class DetailPageFragment : Fragment() {
    private lateinit var binding: FragmentDetailPageBinding
    private var _binding: FragmentDetailPageBinding? = null
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
            fragmentTransaction.setCustomAnimations(R.anim.slide_down_enter, R.anim.slide_down_exit)
                .remove(this).commit()//디테일페이지 사라지기 애니메이션
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
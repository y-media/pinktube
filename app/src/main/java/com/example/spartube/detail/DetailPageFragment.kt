package com.example.spartube.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spartube.databinding.FragmentDetailPageBinding
import com.example.spartube.home.adapter.CategoryAdapter
import com.example.spartube.home.adapter.CategoryChannelAdapter
import com.example.spartube.home.adapter.HomeAdapter

class DetailPageFragment : Fragment() {
    private lateinit var binding: FragmentDetailPageBinding
    private var _binding: FragmentDetailPageBinding? = null
    private lateinit var adapterHome: HomeAdapter
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var adapterChannel: CategoryChannelAdapter


    companion object {

        fun newInstance() = DetailPageFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPageBinding.inflate(inflater, container, false)

//        //뒤로가기
//        binding.btnDetailBack.setOnClickListener{
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            val detailPageFragment = DetailPageFragment
//
//            fragmentTransaction.replace(androidx.fragment.R.id.fragment_container_view_tag, detailPageFragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }

        initShare()
        return binding.root
    }

    private fun initShare() {
        binding.ivDetailShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "http")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            activity?.startActivity(shareIntent)
        }
    }//intent.ACTION_SEND를 이용한 공유 기능

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
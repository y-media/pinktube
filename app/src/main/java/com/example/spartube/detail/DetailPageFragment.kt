package com.example.spartube.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spartube.R
import com.example.spartube.databinding.FragmentDetailPageBinding

class DetailPageFragment : Fragment() {
    private lateinit var binding: FragmentDetailPageBinding
    private var _binding: FragmentDetailPageBinding? = null

    val intent: Intent = Intent(Intent.ACTION_SEND)
    intent.type = ""

    companion object {

        fun newInstance() = DetailPageFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_page, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
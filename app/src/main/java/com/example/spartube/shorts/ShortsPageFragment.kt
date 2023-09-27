package com.example.spartube.shorts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentShortsPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShortsPageFragment : Fragment() {
    private var _binding: FragmentShortsPageBinding? = null
    private val binding: FragmentShortsPageBinding
        get() = _binding!!
    private val shortsList = arrayListOf<BindingModel>()
    private val shortPageRecyclerAdapter by lazy {
        ShortsPageAdapter()
    }
    private var nextPageToken: String? = null
    private val endScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 스크롤이 최하단일 떄
                if (!binding.shortsPageRecyclerView.canScrollVertically(1)
                ) {
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

    companion object {
        fun newInstance() = ShortsPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortsPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
//        val dummy = arrayListOf<BindingModel>()
//        dummy.add(BindingModel("123", "asdasda", "asdasd1234", "123123"))
//        dummy.add(BindingModel("123", "qweqwe", "qweqwe234", "123123"))
//        dummy.add(BindingModel("123", "zxczxc", "zxcxzxc123", "123123"))
//        dummy.add(BindingModel("123", "gfhddf", "dfghdfh123", "123123"))
//        shortsPageViewpager.run {
//            adapter = shortPageRecyclerAdapter
//            orientation = ViewPager2.ORIENTATION_VERTICAL
////            setCurrentItem(shortPageRecyclerAdapter.itemCount / 2, false)
//        }
//        shortPageRecyclerAdapter.addItems(dummy)

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

    private fun setOnClickListener() {

    }

    private fun filteringOverOneMinutes(duration: String): Boolean =
        duration.contains("M")

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

data class BindingModel(
    val linkId: String,
    val channelId: String?,
    val title: String?,
    val replyCount: String?,
    val duration: String?,
)
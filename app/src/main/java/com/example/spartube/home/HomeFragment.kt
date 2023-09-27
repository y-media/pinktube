package com.example.spartube.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentHomeBinding
import com.example.spartube.home.adapter.CategoryAdapter
import com.example.spartube.home.adapter.CategoryChannelAdapter
import com.example.spartube.home.adapter.HomeAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    // Popular video recyclerview adapter
    private val homeAdapter by lazy {
        HomeAdapter()
    }

    // choose category recyclerview adapter
    private val categoryAdapter by lazy {
        CategoryAdapter()
    }

    private val categoryChannelAdapter by lazy{
        CategoryChannelAdapter()
    }

    private val categoriesList = arrayListOf<Triple<String, String, String>>()
    private val modelsList = arrayListOf<BindingModel>()
    private val categoryModels = arrayListOf<BindingModel>()
    private val channelModels = arrayListOf<ChannelBindingModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recycler view 초기화 - adapter
        binding.mainPopularRecyclerView.apply {
            adapter = homeAdapter
        }
        binding.mainCategoryRecyclerView.apply {
            adapter = categoryAdapter
        }
        binding.mainCategoryChannelRecyclerView.apply {
            adapter = categoryChannelAdapter
        }

        // 코루틴을 통해 데이터 가져오기
        CoroutineScope(Dispatchers.IO).launch {
            val responseVideo = RetrofitModule.getVideos()
            val responseCategory = RetrofitModule.getCategories()

            // popular video list 생성
            responseVideo.body()?.let { videos ->
                videos.items.forEach { item ->
                    modelsList.add(
                        BindingModel(
                            id = item.id,
                            publishedAt = item.snippet.publishedAt,
                            thumbnailUrl = item.snippet.thumbnails.default.url,
                            title = item.snippet.title,
                            viewCount = item.statistics.viewCount,
                            runningTime = item.contentDetails.duration
                        )
                    )
                }
            }

            // 카테고리 객체 리스트 생성 - 카테고리 별 id 값으로 검색을 해야하기 때문에 (maybe) id 값을 같이 저장
            responseCategory.body()?.let { categories ->
                categories.items.forEach { item ->
                    /**
                     *   assignable이 true인 것만 카테고리 api에 접속 가능하기 떄문에 필터링
                     *
                     *   snippet.assignable	boolean
                     *   동영상을 카테고리와 연결할 수 있는지를 나타냅니다.
                     */
                    if (item.snippet.assignable)
                        categoriesList.add(
                            Triple(
                                item.snippet.title,
                                item.id,
                                item.snippet.channelId
                            )
                        )
                }
            }

            // 프래그먼트가 액티비티에 붙어있다면
            if (isAdded) {
                requireActivity().runOnUiThread {
                    // popular video를 리사이클러뷰에 바인딩
                    homeAdapter.addItems(modelsList)
                }
                // 스피너에 카테고리 목록을 추가
                binding.mainSpinnerView.setItems(categoriesList.map { it.first })
            }
        }

        binding.mainSpinnerView.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            // 스피너 클릭 했을 떄, 이전 카테고리에 대한 목록을 전부 삭제
            categoryModels.clear()
            channelModels.clear()

            // 클릭한 카테고리 정보를 찾음
            val categoryInfo = categoriesList.find { it.first == text }

            // 코루틴
            CoroutineScope(Dispatchers.IO).launch {
                val responseCategoryList = RetrofitModule.getVideos(categoryInfo?.second)
//                val channelRes = RetrofitModule.getVideosByChannel(categoryInfo?.third)
                // 카테고리별 *영상*을 가져옴
                responseCategoryList.body()?.let { categoryList ->
                    categoryList.items.forEach { item ->
                        categoryModels.add(
                            BindingModel(
                                id = item.id,
                                publishedAt = item.snippet.publishedAt,
                                thumbnailUrl = item.snippet.thumbnails.default.url,
                                title = item.snippet.title,
                                viewCount = item.statistics.viewCount,
                                runningTime = item.contentDetails.duration
                            )
                        )
                    }
                }

                if (isAdded) {
                    requireActivity().runOnUiThread {
                        // 카테고리별 영상 리사이클러뷰에 데이터 바인딩
                        categoryAdapter.addItems(categoryModels)
                    }
                }
            }

//            CoroutineScope(Dispatchers.IO).launch {
//                val channelRes = RetrofitModule.getVideosByChannel(categoryInfo?.third)
//                // 카테고리별 *채널 목록* 가져오기
//                channelRes.body()?.let { channelList ->
//                    channelList.items.forEach { item ->
//                        channelModels.add(
//                            ChannelBindingModel(
//                                id = item.id,
//                                channelId = item.snippet.channelId,
//                                channelTitle = item.snippet.channelTitle,
//                                url = item.snippet.thumbnails.default.url
//                            )
//                        )
//                    }
//                }
//                if (isAdded) {
//                    requireActivity().runOnUiThread {
//                        // 카테고리별 영상 리사이클러뷰에 데이터 바인딩
//                        categoryChannelAdapter.addItems(channelModels)
//                    }
//                }
//            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

data class BindingModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val thumbnailUrl: String,
    @SerializedName("duration")
    val runningTime: String?,
    @SerializedName("viewCount")
    val viewCount: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
)

data class ChannelBindingModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("channelTitle")
    val channelTitle: String,
    @SerializedName("url")
    val url: String,
)


package com.example.spartube.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.spartube.R
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentHomeBinding
import com.example.spartube.detail.DetailPageFragment
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

    // choose channel recyclerview adapter
    private val categoryChannelAdapter by lazy {
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

    fun clickPopular() {
        homeAdapter.setOnClickListener(object : HomeAdapter.ItemClick {
            override fun onClick(view: View, Position: Int, model: BindingModel) {
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val detailPageFragment = DetailPageFragment.newInstance().apply {
                    arguments = Bundle().apply {
                        putString("model_id", model.id)
                        putString("model_title", model.title)

                        putString("model_url", model.thumbnailUrl)

                        putString("model_content", model.description)

                    }
                }
                val requireFragment =
                    requireActivity().findViewById<FragmentContainerView>(R.id.container_detail)
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_up_enter,
                    R.anim.slide_down_exit
                ).replace(R.id.container_detail, detailPageFragment).commit() //디테일페이지 띄우기 애니메이션 추가
                requireFragment.isVisible =
                    true
                requireActivity().findViewById<ViewPager2>(R.id.activity_main_viewpager).isVisible =
                    false
                println(model)
            }
        }
        )
    }

    fun clickCategory() {
        categoryAdapter.categoryClickListener(object : CategoryAdapter.ItemClick {
            override fun onClick(view: View, Position: Int, model: BindingModel) {
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val detailPageFragment = DetailPageFragment.newInstance().apply {
                    arguments = Bundle().apply {
                        putString("model_id", model.id)
                        putString("model_title", model.title)
                        putString("model_content", model.description)
                        putString("model_url", model.thumbnailUrl)
                    }
                }
                val requireFragment =
                    requireActivity().findViewById<FragmentContainerView>(R.id.container_detail)
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_up_enter, R.anim.slide_down_exit
                ).replace(R.id.container_detail, detailPageFragment).commit()
                requireFragment.isVisible = true
                requireActivity().findViewById<ViewPager2>(R.id.activity_main_viewpager).isVisible =
                    false
            }
        }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // recycler view 초기화 - adapter
        binding.mainPopularRecyclerView.apply {
            adapter = homeAdapter
            clickPopular() //클릭이벤트 인기순 영상
        }
        binding.mainCategoryRecyclerView.apply {
            adapter = categoryAdapter
            clickCategory() //클릭이벤트 종류별 영상
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
                            runningTime = item.contentDetails.duration,
                            description = item.snippet.description
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

                // 스피너 카테고리 목록의 색깔 추가
                val backgroundColor = context?.let { ContextCompat.getColor(it, R.color.pink3) }
                if (backgroundColor != null) {
                    binding.mainSpinnerView.setBackgroundColor(backgroundColor)
                }
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
//                 카테고리별 *영상*을 가져옴
                responseCategoryList.body()?.let { categoryList ->
                    categoryList.items.forEach { item ->
                        categoryModels.add(
                            BindingModel(
                                id = item.id,
                                publishedAt = item.snippet.publishedAt,
                                thumbnailUrl = item.snippet.thumbnails.default.url,
                                title = item.snippet.title,
                                viewCount = item.statistics.viewCount,
                                runningTime = item.contentDetails.duration,
                                description = item.snippet.description
                            )
                        )
//                        카테고리에 해당하는 각 *채널* 을 가져옴
                        val channelRes = RetrofitModule.getVideosByChannel(item.snippet.channelId)
                        channelRes.body()?.let { channel ->
                            channel.items.forEach { item ->
                                channelModels.add(
                                    ChannelBindingModel(
                                        id = item.id,
                                        thumbnailUrl = item.snippet.thumbnails.default.url,
                                        title = item.snippet.title
                                    )
                                )
                            }
                        }
                    }
                }

                if (isAdded) {
                    requireActivity().runOnUiThread {
                        // 카테고리별 영상 리사이클러뷰에 데이터 바인딩
                        categoryAdapter.addItems(categoryModels)
                        // 카테고리별 채널 리사이클러뷰에 데이터 바인딩
                        categoryChannelAdapter.addItems(channelModels)
                    }
                }
            }
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
    @SerializedName("description")
    val description: String,
)

data class ChannelBindingModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val thumbnailUrl: String,
)


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.spartube.R
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentSearchBinding
import com.example.spartube.detail.DetailPageFragment
import com.example.spartube.home.BindingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.spartube.search.SearchPageEntity as SearchPageEntity

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var editText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val rootView = binding.root
        editText = binding.etSearchEdittext
        searchButton = binding.ibSearchSearchbutton
        recyclerView = binding.rbSearchRecyclerview

        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)


        // 검색 버튼 클릭 이벤트 처리
        searchButton.setOnClickListener {
            val searchText = editText.text.toString()
            getSearchResults(searchText)
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etSearchEdittext.windowToken, 0) //키보드 내리기
        }

        return rootView
    }

    private fun getSearchResults(query: String){

        val arraylist = arrayListOf<SearchPageEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitModule.getSearchResult(query)
            response.body()?.let { searchResults ->
                searchResults.items.forEach{item ->
                    arraylist.add(
                        SearchPageEntity(
                            title = item.snippet.title,
                            videoId = item.id.videoId,
                            description = item.snippet.description,
                            publishedAt = item.snippet.publishedAt,
                            thumbnails = item.snippet.thumbnails.high.url
                        )
                    )
                }
            }
            requireActivity().runOnUiThread {
                val adapter = SearchPageAdapter(arraylist)
                recyclerView.adapter = adapter
                adapter.setOnClickListener(object :SearchPageAdapter.ItemClick{
                    override fun onClick(view: View, model: BindingModel) {
                        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        val detailPageFragment = DetailPageFragment.newInstance().apply {
                            arguments = Bundle().apply {
                                putString("model_id", model.id)
                                putString("model_title", model.title)
                                putString("model_content", model.description)
                                putString("model_url",model.thumbnailUrl)
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

                })
                adapter.notifyDataSetChanged()
            }
        }
    }

}

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.FragmentSearchBinding
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
                            thumbnails = item.snippet.thumbnails.default.url
                        )
                    )
                }
            }
            requireActivity().runOnUiThread {
                val adapter = SearchPageAdapter(arraylist)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

}

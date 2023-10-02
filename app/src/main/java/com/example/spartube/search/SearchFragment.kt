import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.FragmentSearchBinding
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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 검색 버튼 클릭 이벤트 처리
        searchButton.setOnClickListener {
            val searchText = editText.text.toString()
            val newSearchResults = getSearchResults(searchText)
            val adapter = SearchPageAdapter(newSearchResults)
            recyclerView.adapter = adapter
        }

        return rootView
    }

    private fun getSearchResults(query: String): List<SearchPageEntity> {
        val dummyData = mutableListOf<SearchPageEntity>()
        for (i in 1..10) {
            val entity = SearchPageEntity("Item $i") // 예시로 "Item $i"와 같은 제목을 설정
            dummyData.add(entity)
        }
        return dummyData
    }

}

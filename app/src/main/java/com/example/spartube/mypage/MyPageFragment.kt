import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.R
import com.example.spartube.db.MyPageEntity
import com.example.spartube.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    // View Binding을 사용한 프로퍼티 선언
    private lateinit var binding: FragmentMyPageBinding

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // View Binding을 초기화하고 루트 뷰를 반환
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerView: RecyclerView = binding.mypageRecyclerView


        binding.mypageImageViewIc.clipToOutline = true


        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MyPageAdapter(getDummyData()) // 더미 데이터를 어댑터에 전달

        return rootView
    }

    private fun getDummyData(): List<MyPageEntity> {
        // 여기에서 더미 데이터를 생성하여 반환
        val dummyData = mutableListOf<MyPageEntity>()
        val oni = R.drawable.oni

        for (i in 1..10) {
            dummyData.add(
                MyPageEntity(
                    id = 1,
                    thumbnailUrl = "android.resource://${requireContext().packageName}/$oni",
                    title = "oni",
                    description = "oni Hunter"
                )
            )
        }
        return dummyData
    }
}

package com.example.spartube.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.FragmentMyPageBinding
import com.example.spartube.db.AppDatabase
import com.example.spartube.db.MyPageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageFragment : Fragment() {
    // View Binding을 사용한 프로퍼티 선언
    private lateinit var binding: FragmentMyPageBinding
    private lateinit var myPageAdapter: MyPageAdapter

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        val rootView = binding.root

        val recyclerView: RecyclerView = binding.myPageRecyclerview

        loadItemsFromDatabase()

        // 그리드 레이아웃 매니저 설정 (2열로 그리드)
        val spanCount = 2 // 한 줄에 보여질 아이템 수
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView.layoutManager = layoutManager

        myPageAdapter = MyPageAdapter(mutableListOf())  { item, longClick ->
            if (longClick) {
                showDeleteConfirmationDialog(item)
            } else {
                // 클릭 이벤트에 대한 작업 수행
            }
        }
        recyclerView.adapter = myPageAdapter // 어댑터를 설정

//        deleteAllItems()

        return rootView

    }

    //데이터가 없을시 보여줄 화면
    fun showAni(isEmpty: Boolean){
        binding.myPageIvEmpty.visibility = if(isEmpty) View.VISIBLE else View.INVISIBLE
        binding.myPageTvEmptyJp.visibility = if(isEmpty) View.VISIBLE else View.INVISIBLE
        binding.myPageTvEmptyKr.visibility = if(isEmpty) View.VISIBLE else View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        // Fragment가 다시 활성화될 때 데이터를 다시 불러오기
        loadItemsFromDatabase()
    }

    //삭제 다이얼로그
    private fun showDeleteConfirmationDialog(item: MyPageEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("이 항목을 삭제할꺼야?")
            .setPositiveButton("응") { dialog, _ ->
                // 확인 버튼을 눌렀을 때 아이템 삭제 작업 수행
                deleteItem(item)
                dialog.dismiss()
            }
            .setNegativeButton("아니") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun loadItemsFromDatabase() {
        val dao = AppDatabase.getDatabase(requireContext()).myPageDao()

        CoroutineScope(Dispatchers.IO).launch {
            val items = dao.getAllVideos()

            requireActivity().runOnUiThread {

                if (items.isEmpty()){
                    showAni(true)
                }else{
                    // 데이터베이스에서 가져온 아이템을 어댑터에 추가하고 업데이트
                    myPageAdapter.addItems(items)
                    myPageAdapter.notifyDataSetChanged()
                    showAni(false)
                }
            }
        }
    }

//    private fun addDataToDatabase(item: MyPageEntity) {
//        val dao = AppDatabase.getDatabase(requireContext()).myPageDao()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            dao.insertVideo(item)
//
//            requireActivity().runOnUiThread {
//                // 데이터를 추가한 후에 어댑터를 업데이트하고 화면을 다시 그립니다.
//                loadItemsFromDatabase()
//            }
//        }
//    }


    private fun deleteItemFromDatabase(item: MyPageEntity) {
        // 데이터베이스 DAO 인터페이스를 사용하여 아이템 삭제
        val dao = AppDatabase.getDatabase(requireContext()).myPageDao()

        // 아이템 삭제 작업 실행 (비동기로 실행하는 것을 권장합니다)
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteVideo(item)

            // 아이템 삭제 작업 완료 후에 데이터베이스에서 데이터를 다시 불러옵니다.
            loadItemsFromDatabase()
        }
    }


    // 아이템 삭제 함수
    private fun deleteItem(item: MyPageEntity) {
        // 데이터베이스에서 아이템 삭제
        deleteItemFromDatabase(item)

        // 어댑터에서 아이템 삭제
        myPageAdapter.removeItem(item)

        // 어댑터에 변경 사항 알림
        myPageAdapter.notifyDataSetChanged()
    }

    private fun deleteAllItems() {
        val dao = AppDatabase.getDatabase(requireContext()).myPageDao()
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllVideos()
        }
    }

}

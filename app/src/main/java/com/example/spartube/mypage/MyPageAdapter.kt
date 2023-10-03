package com.example.spartube.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spartube.databinding.ItemRecyclerHomeBinding
import com.example.spartube.db.MyPageEntity

class MyPageAdapter(
    private val data: MutableList<MyPageEntity>,
    private val itemClickListener: (MyPageEntity, Boolean) -> Unit) :
    RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecyclerHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemRecyclerHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyPageEntity) = with(binding){
            itemTitleTextView.text = item.title
            itemThumbnailImageView.load(item.thumbnailUrl){
                size(500, 400)
            }



// 롱클릭 이벤트 처리
            root.setOnLongClickListener {
                itemClickListener(item, true)
                true // 롱클릭 이벤트 처리를 여기서 완료하므로 true를 반환합니다.
            }

// 다른 클릭 이벤트 처리
            root.setOnClickListener {
                itemClickListener(item, false)
            }
        }
    }

    // 아이템 삭제 함수
    fun removeItem(item: MyPageEntity) {
        val position = data.indexOf(item)
        if (position != -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

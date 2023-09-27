package com.example.spartube.home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spartube.databinding.ItemRecyclerHomeBinding
import com.example.spartube.home.BindingModel
import com.example.spartube.home.ChannelBindingModel

class CategoryChannelAdapter: RecyclerView.Adapter<CategoryChannelAdapter.ViewHolder>() {

    private val list = arrayListOf<ChannelBindingModel>()

    fun addItems(items: List<ChannelBindingModel>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: ItemRecyclerHomeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ChannelBindingModel) = with(binding){
            itemTitleTextView.text = item.channelId
            // coil 사용해보기
            Glide.with(binding.root)
                .load(Uri.parse(item.url))
                .fitCenter()
                .override(500, 400)
                .into(itemThumbnailImageView)
        }
    }

}
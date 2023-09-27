package com.example.spartube.home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spartube.databinding.ItemRecyclerHomeBinding
import com.example.spartube.home.BindingModel
import java.text.DecimalFormat

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private val list = arrayListOf<BindingModel>()

    fun addItems(items: List<BindingModel>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemRecyclerHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        holder.bind(list[position])

    }

    inner class HomeViewHolder(private val binding: ItemRecyclerHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BindingModel) = with(binding) {
            itemTimeTextView.text = model.runningTime
            itemCountTextView.text = model.viewCount + "회"
            itemTitleTextView.text = model.title
            itemDateTextView.text = model.publishedAt
            Glide.with(binding.root)
                .load(Uri.parse(model.thumbnailUrl))
                .fitCenter()
                .override(500, 400)
                .into(itemThumbnailImageView)

        }
    }
}
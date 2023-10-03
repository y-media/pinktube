package com.example.spartube.home.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spartube.databinding.ItemRecyclerHomeBinding
import com.example.spartube.home.BindingModel
import java.text.DecimalFormat

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.HomeViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, Position: Int, model: BindingModel)
    }

    var itemClick: ItemClick? = null
    fun categoryClickListener(listener: ItemClick) {
        itemClick = listener
    }

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
        //click event at Category RecyclerView in HomeFragment
        holder.itemView.setOnClickListener() {
            itemClick?.onClick(it, position, list[position])
        }
        holder.bind(list[position])
    }

    inner class HomeViewHolder(private val binding: ItemRecyclerHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val decimalFormat = DecimalFormat("#,###")

        @SuppressLint("SetTextI18n")
        fun bind(model: BindingModel) = with(binding) {
            itemTimeTextView.text = model.runningTime?.let { HomeAdapter().formatRunningTime(it) }
            itemCountTextView.text = decimalFormat.format(model.viewCount.toInt()) + "회 ·"
            itemTitleTextView.text = model.title
            itemDateTextView.text = HomeAdapter().extractDate(model.publishedAt)
            Glide.with(binding.root)
                .load(Uri.parse(model.thumbnailUrl))
                .fitCenter()
                .override(500, 400)
                .into(itemThumbnailImageView)

        }
    }
}
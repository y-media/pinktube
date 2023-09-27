package com.example.spartube.shorts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ShortsPageItemBinding
import com.example.spartube.shorts.listener.ShortsItemClickListener

class ShortsPageAdapter : RecyclerView.Adapter<ShortsPageAdapter.ShortsViewHolder>() {
    private val list = arrayListOf<BindingModel>()
    private lateinit var shortsItemClickListener: ShortsItemClickListener
    fun addItems(items: List<BindingModel>) {
//        list.clear()
        list.addAll(items)
        notifyItemInserted(items.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsViewHolder {
        return ShortsViewHolder(
            ShortsPageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
//        return Int.MAX_VALUE
        return list.size
    }

    override fun onBindViewHolder(holder: ShortsViewHolder, position: Int) {
//        val actualPosition = position % list.size
//        holder.bind(list[actualPosition])
        holder.bind(list[position])
    }

    inner class ShortsViewHolder(private val binding: ShortsPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                shortsItemClickListener.onShortsItemClick(bindingAdapterPosition)
            }
        }

        fun bind(model: BindingModel) = with(binding) {
            shortsTitleView.text = model.title
            "@${model.channelId}".also { shortsChannelIdTextView.text = it }
        }
    }
}
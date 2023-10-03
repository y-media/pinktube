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

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    //아이템 클릭
    interface ItemClick {
        fun onClick(view: View, Position: Int, model: BindingModel)
    }

    var itemClick: ItemClick? = null
    fun setOnClickListener(listener: ItemClick) {
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
        //클릭
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position, list[position])
            println(position)
        }
        holder.bind(list[position])
    }

    inner class HomeViewHolder(private val binding: ItemRecyclerHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val decimalFormat = DecimalFormat("#,###")

        @SuppressLint("SetTextI18n")
        fun bind(model: BindingModel) = with(binding) {
            itemTimeTextView.text = model.runningTime?.let { formatRunningTime(it) }
            itemCountTextView.text = decimalFormat.format(model.viewCount.toInt()) + "회 ·"
            itemTitleTextView.text = model.title
            itemDateTextView.text = extractDate(model.publishedAt)

//            itemThumbnailImageView.load(model.thumbnailUrl){
//                RoundedCornersTransformation()
//                size(500, 400)
//            }

            Glide.with(binding.root)
                .load(Uri.parse(model.thumbnailUrl))
                .fitCenter()
                .override(500, 400)
                .into(itemThumbnailImageView)

        }
    }

    fun formatRunningTime(runningTime: String): String {
        val regex = Regex("""PT(\d+H)?(\d+M)?(\d+S)?""")
        val matchResult = regex.find(runningTime)

        val hours = matchResult?.groups?.get(1)?.value?.dropLast(1)?.toInt() ?: 0
        val minutes = matchResult?.groups?.get(2)?.value?.dropLast(1)?.toInt() ?: 0
        val seconds = matchResult?.groups?.get(3)?.value?.dropLast(1)?.toInt() ?: 0

        return buildString {
            if (hours > 0) append("${hours}:")
            if (minutes > 0) {
                append("${minutes}:")
            } else {
                append("00:")
            }
            if (seconds in 1..9) {
                append("0$seconds")
            } else if (seconds > 9) {
                append("$seconds")
            } else {
                append("00")
            }
        }
    }

    fun extractDate(publishedAt: String): String {
        return publishedAt.substring(0, 10)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getRelativeTimeAgo(publishedAt: String): String {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
//        val instant = Instant.from(formatter.parse(publishedAt))
//
//        // Instant를 LocalDateTime으로 변환 후 LocalDate로 추출
//        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
//        val publishedDate = localDateTime.toLocalDate()
//
//        val currentDate = LocalDateTime.now().toLocalDate()
//
//        val years = abs(currentDate.year - publishedDate.year)
//        val months = abs(currentDate.monthValue - publishedDate.monthValue)
//        val weeks = abs(currentDate.toEpochDay() - publishedDate.toEpochDay()) / 7
//        val days = abs(currentDate.toEpochDay() - publishedDate.toEpochDay())
//
//        return when {
//            years > 0 -> "$years 년 전"
//            months > 0 -> "$months 년 전"
//            weeks > 0 -> "$weeks 년 전"
//            days > 0 -> "$days 년 전"
//            else -> "방금 전"
//        }
//    }
}
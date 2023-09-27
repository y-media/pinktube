import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.databinding.ItemRecyclerHomeBinding
import com.example.spartube.db.MyPageEntity

class MyPageAdapter(private val data: List<MyPageEntity>) :
    RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemRecyclerHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyPageEntity) {
            binding.itemTitleTextView.text = item.title

        }
    }
}

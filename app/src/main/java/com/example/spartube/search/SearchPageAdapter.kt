import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.R
import com.example.spartube.search.SearchPageEntity

class SearchPageAdapter(private val data: List<SearchPageEntity>) :
    RecyclerView.Adapter<SearchPageAdapter.ViewHolder>() {

    // 뷰홀더 클래스 정의
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView) // 예시로 제목 텍스트뷰
        // 다른 필요한 뷰들을 여기에 추가할 수 있습니다.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.search_page_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]

        // 데이터를 뷰에 바인딩
        holder.titleTextView.text = currentItem.title // 예시로 제목을 텍스트뷰에 설정

        // 다른 데이터를 다른 뷰에 설정할 수 있습니다.
    }

    override fun getItemCount() = data.size
}

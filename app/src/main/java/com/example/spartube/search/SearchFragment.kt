import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.spartube.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val tabLayout: TabLayout = findViewById(R.id.fragment_search_tab)
//        val viewPager: ViewPager2 = findViewById(R.id.fragment_search_viewPager)


//        val pagerAdapter = MyPagerAdapter(this)
//        viewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager2를 연결
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // 각 탭의 제목 설정
//            tab.text = "Tab ${position + 1}"
//        }.attach()
    }
}

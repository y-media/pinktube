package com.example.spartube.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.spartube.home.HomeFragment
import com.example.spartube.R
import com.example.spartube.search.SearchFragment

class MainViewPagerAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments = ArrayList<MainTab>()

    init {
        fragments.add(
            MainTab(
                HomeFragment.newInstance(),
            R.string.home,
            R.drawable.ic_home
            )
        )
        fragments.add(
            MainTab(
                SearchFragment.newInstance(),
            R.string.search,
            R.drawable.ic_search
            )
        )
        fragments.add(
            MainTab(
                HomeFragment.newInstance(),
            R.string.my_page,
            R.drawable.ic_my_page
            )
        )
        fragments.add(
            MainTab(
                HomeFragment.newInstance(),
            R.string.shorts,
            R.drawable.ic_shorts
            )
        )
    }

    fun getIcon(position: Int): Int{
        return fragments[position].icon
    }

    fun getTitle(position: Int): Int{
        return fragments[position].title
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
}
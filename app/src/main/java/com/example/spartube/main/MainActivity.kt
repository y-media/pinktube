package com.example.spartube.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spartube.databinding.ActivityMainBinding
import com.example.spartube.main.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {

        activityMainViewpager.adapter = viewPagerAdapter
        TabLayoutMediator(activityMainTab, activityMainViewpager){tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
            tab.setIcon(viewPagerAdapter.getIcon(position))
        }.attach()

    }
}



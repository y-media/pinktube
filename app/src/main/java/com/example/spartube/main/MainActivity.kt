package com.example.spartube.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.spartube.databinding.ActivityMainBinding
import com.example.spartube.util.ConnectWatcher
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        activityMainViewpager.adapter = viewPagerAdapter
        TabLayoutMediator(activityMainTab, activityMainViewpager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
            tab.setIcon(viewPagerAdapter.getIcon(position))
        }.attach()
    }

    private fun initViewModel() = with(binding) {
        ConnectWatcher(this@MainActivity).observe(this@MainActivity) { connection ->
            mainViewModel.setNetworkStatus(connection)
        }
        mainViewModel.networkStatus.observe(this@MainActivity) { isAvailable ->
            networkTextView.isVisible = !isAvailable
            networkProgressBar.isVisible = !isAvailable
            activityMainViewpager.isVisible = isAvailable
            activityMainTab.isEnabled = isAvailable
        }
    }
}



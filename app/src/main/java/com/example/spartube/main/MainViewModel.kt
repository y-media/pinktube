package com.example.spartube.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // 네트워크 상태를 살피는 LiveData
    private val _networkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val networkStatus: LiveData<Boolean>
        get() = _networkStatus

    // 상태 설정
    fun setNetworkStatus(isAvailable: Boolean) {
        _networkStatus.value = isAvailable
    }
}
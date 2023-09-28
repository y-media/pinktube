package com.example.spartube.shorts.util

import android.app.Activity
import android.content.Context

class PreferenceUtils(context: Context) {
    private val prefs = context.getSharedPreferences(TIME_PREFS_NAME, Activity.MODE_PRIVATE)

    var time: Long?
        get() = prefs.getLong(TIME_PREFS_NAME, 0L)
        set(value) {
            if (value != null) {
                prefs.edit().putLong(TIME_PREFS_NAME, value).apply()
            }
        }


    companion object {
        const val TIME_PREFS_NAME = "prefsPlay"
    }
}
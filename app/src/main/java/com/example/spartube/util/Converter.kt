package com.example.spartube.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Converter {
    fun getDateFromTimestampWithFormat(
        timestamp: String?,
    ): String {
        var date: Date? = null
        var res = ""
        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
            date = timestamp?.let { format.parse(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        res = date?.let { df.format(it) }.toString()
        return res
    }
}
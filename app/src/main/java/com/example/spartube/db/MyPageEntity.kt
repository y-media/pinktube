package com.example.spartube.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyPageEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo
    val thumbnailUrl: String?,
    @ColumnInfo
    val title: String?
)
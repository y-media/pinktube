package com.example.spartube.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyPageDao {

    @Query("SELECT * FROM MyPageEntity")
    fun getAllVideos(): List<MyPageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo(model: MyPageEntity)

    @Delete
    fun deleteVideo(model: MyPageEntity)

    @Query("DELETE FROM MyPageEntity")
    fun deleteAllVideos()
}
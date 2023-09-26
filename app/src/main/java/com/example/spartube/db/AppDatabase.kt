package com.example.spartube.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyPageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myPageDao(): MyPageDao

    companion object {
        private var INSTANCE: AppDatabase? = null
    }

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context, AppDatabase::class.java, "my_page_db"
            ).build()
        }
        return INSTANCE as AppDatabase
    }
}
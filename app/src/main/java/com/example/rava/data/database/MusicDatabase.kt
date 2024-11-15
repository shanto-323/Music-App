package com.example.rava.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rava.data.dao.Dao
import com.example.rava.data.database.model.MusicEntity

@Database(entities = [MusicEntity::class], version = 1, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {
  abstract fun musicDao(): Dao
}
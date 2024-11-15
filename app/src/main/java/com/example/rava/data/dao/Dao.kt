package com.example.rava.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rava.data.database.model.MusicEntity

@Dao
interface Dao {
  @Query("SELECT * FROM music_table ORDER BY id ASC")
  fun getAllMusic(): PagingSource<Int, MusicEntity>


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllMusic(musicFiles: List<MusicEntity>)
}
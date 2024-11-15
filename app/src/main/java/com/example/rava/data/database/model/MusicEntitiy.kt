package com.example.rava.data.database.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
  tableName = "music_table"
)
data class MusicEntity(
  @PrimaryKey(autoGenerate = false)
  val id: Int = 0,

  val title: String,
  val artist: String,
  val path: String,
  val duration: Long,
  val albumArtUri: String
)
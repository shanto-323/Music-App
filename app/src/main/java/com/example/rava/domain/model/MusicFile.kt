package com.example.rava.domain.model

import android.net.Uri

data class MusicFile(
  val id: Long = 0,
  val title: String = "",
  val artist: String = "",
  val path: String = "",
  val duration: Long = 0,
  val albumArtUri: String = ""
)
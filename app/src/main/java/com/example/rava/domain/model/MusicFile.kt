package com.example.rava.domain.model

import android.net.Uri

data class MusicFile(
  val id: Long,
  val title: String,
  val artist: String,
  val path: String,
  val duration: Long,
  val albumArtUri: Uri
)
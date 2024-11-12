package com.example.rava.presentation.home.items

import com.example.rava.domain.model.MusicFile

data class State(
  val musicFiles: List<MusicFile> = emptyList()
)
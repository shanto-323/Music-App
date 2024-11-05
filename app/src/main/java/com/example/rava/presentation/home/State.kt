package com.example.rava.presentation.home

import com.example.rava.domain.model.MusicFile

data class State(
  val musicFiles: List<MusicFile> = emptyList()
)
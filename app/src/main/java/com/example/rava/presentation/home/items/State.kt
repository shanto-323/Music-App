package com.example.rava.presentation.home.items

import androidx.paging.PagingData
import com.example.rava.domain.model.MusicFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class State(
  val music: MusicFile = MusicFile(),
  val isPlaying: Boolean = false,
  val timeOfMusic: Long = 0,
  val currentIndexOfMusic: Long = 0,
  val musicFiles: StateFlow<PagingData<MusicFile>> = MutableStateFlow(PagingData.empty())
)
package com.example.rava.presentation.home.statechange

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.paging.PagingData
import com.example.rava.domain.model.MusicFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class State(
  val music: MusicFile = MusicFile(),
  val isPlaying: Boolean = false,
  val timeOfMusic: Long = 0,
  val currentIndexOfMusic: Long = 0,
  val musicFiles: StateFlow<PagingData<MusicFile>> = MutableStateFlow(PagingData.empty()),
  val playPauseIcon : ImageVector = Icons.Default.PlayArrow
)
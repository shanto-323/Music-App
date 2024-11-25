package com.example.rava.presentation.home.StateChange

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rava.domain.model.MusicFile

sealed class Event {
  data class Music(val music: MusicFile) : Event()
  data class IsPlaying(val isPlaying: Boolean) : Event()
  data class TimeOfMusic(val timeOfMusic: Long) : Event()
  data class CurrentIndexOfMusic(val currentIndexOfMusic: Long) : Event()
  data class PlayPauseIcon(val playPauseIcon: ImageVector) : Event()
}
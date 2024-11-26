package com.example.rava.presentation.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.cachedIn
import com.example.rava.domain.model.MusicFile
import com.example.rava.domain.repository.DatabaseRepository
import com.example.rava.presentation.home.statechange.Event
import com.example.rava.presentation.home.statechange.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: DatabaseRepository,
  val exoPlayer: ExoPlayer,
) : ViewModel() {

  var state by mutableStateOf(State())

  fun onEvent(e: Event) {
    when (e) {
      is Event.CurrentIndexOfMusic -> state =
        state.copy(currentIndexOfMusic = e.currentIndexOfMusic)

      is Event.IsPlaying -> {
        state = state.copy(isPlaying = e.isPlaying)
      }

      is Event.Music -> state = state.copy(music = e.music)
      is Event.TimeOfMusic -> state = state.copy(timeOfMusic = e.timeOfMusic)
      is Event.PlayPauseIcon -> state = state.copy(playPauseIcon = e.playPauseIcon)
    }
  }

  init {
    getMusic()
  }

  fun insertMusicFiles(context: Context) {
    viewModelScope.launch {
      repository.insertMusicFiles(context)
      repository.getAllMusic()
    }
  }

  fun getMusic() {
    viewModelScope.launch {
      repository.getAllMusic()
        .cachedIn(viewModelScope)
        .collect {
          state = state.copy(
            musicFiles = MutableStateFlow(it)
          )
        }
    }
  }


  fun exoplayerInstance(uri: String) {
    val mediaItem = MediaItem.fromUri(uri)
    exoPlayer.apply {
      setMediaItem(mediaItem)
      prepare()
    }
  }

}
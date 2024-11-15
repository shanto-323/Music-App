package com.example.rava.presentation.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.rava.domain.repository.Repository
import com.example.rava.presentation.home.items.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: Repository,
  val exoPlayer: ExoPlayer,
) : ViewModel() {
  var state by mutableStateOf(State())
    private set

  fun getMusic(context: Context) {
    state = state.copy(
      musicFiles = repository.getMusicFiles(context)
    )
  }

  fun exoplayerInstance(uri: String) {
    val mediaItem = MediaItem.fromUri(uri)
    exoPlayer.apply {
      setMediaItem(mediaItem)
      prepare()
      playWhenReady = true
    }
  }
}
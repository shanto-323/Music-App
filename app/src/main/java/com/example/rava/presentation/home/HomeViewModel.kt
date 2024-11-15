package com.example.rava.presentation.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingData
import com.example.rava.domain.model.MusicFile
import com.example.rava.domain.repository.DatabaseRepository
import com.example.rava.domain.repository.Repository
import com.example.rava.presentation.home.items.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: DatabaseRepository,
  val exoPlayer: ExoPlayer,
) : ViewModel() {
  private val _musicFiles = MutableStateFlow<PagingData<MusicFile>>(PagingData.empty())
  val musicFiles: StateFlow<PagingData<MusicFile>> = _musicFiles

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
      repository.getAllMusic().collect {
        _musicFiles.value = it
      }
    }
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
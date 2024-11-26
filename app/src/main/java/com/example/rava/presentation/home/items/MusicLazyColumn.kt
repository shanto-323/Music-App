package com.example.rava.presentation.home.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.rava.domain.model.MusicFile
import com.example.rava.presentation.home.HomeViewModel
import com.example.rava.presentation.home.statechange.Event

@Composable
fun MusicLazyColumn(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  viewModel: HomeViewModel
) {
  val musicFiles = viewModel.state.musicFiles.collectAsLazyPagingItems()
  LazyColumn(
    modifier.fillMaxSize()
  ) {
    if (musicFiles != null) {
      items(musicFiles.itemSnapshotList.size) { music ->
        MusicCard(
          music = musicFiles[music]!!,
          isOpen = {
            if (music != null && viewModel.state.music != musicFiles[music]!!) {
              viewModel.onEvent(Event.IsPlaying(isPlaying = true))
            }
            viewModel.onEvent(Event.Music(musicFiles[music]!!))
            onClick()
          }
        )
      }
      item {
        Box(
          modifier = Modifier.size(180.dp)
        ){}
      }
    } else {
      item {
        Text(text = "no music found")
      }
    }
  }
}
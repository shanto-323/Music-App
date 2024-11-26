package com.example.rava.presentation.home.items

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.rava.domain.model.MusicFile
import com.example.rava.presentation.home.HomeViewModel
import com.example.rava.presentation.home.statechange.Event

@Composable
fun MusicLazyColumn(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  musicFiles: LazyPagingItems<MusicFile>,
  musicPlay: (MusicFile) -> Unit,
  viewModel: HomeViewModel
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize()
  ) {
    if (musicFiles != null) {
      items(musicFiles.itemSnapshotList.size) { music ->
        Text(text = musicFiles.itemSnapshotList[music]!!.artist.toString())
//        MusicCard(
//          onClick = {
//            viewModel.onEvent(Event.Music(music = musicFiles[music]!!))
//            onClick()
//
////            musicPlay(musicFiles[music]!!)
//          }
//        )
      }
    } else {
      item {
        Text(text = "no music found")
      }
    }
  }
}
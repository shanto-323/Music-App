package com.example.rava.presentation.home.items

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.rava.domain.model.MusicFile
import kotlinx.coroutines.launch

@Composable
fun MusicLazyColumn(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  musicFiles: List<MusicFile>?,
  musicPlay: (MusicFile) -> Unit
) {
  LazyColumn(
    modifier.fillMaxSize()
  ) {
    if (musicFiles != null) {
      items(musicFiles.size) { music ->
        MusicCard(
          musicFile = musicFiles[music],
          isOpen = {
            onClick()
            musicPlay(musicFiles[music])
          }
        )
      }
    } else {
      item {
        Text(text = "no music found")
      }
    }
  }
}
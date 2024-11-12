package com.example.rava.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(
  modifier: Modifier = Modifier,
  musicId: Int
) {
  var sheetState = rememberModalBottomSheetState()
  var isSheetOpen by rememberSaveable {
    mutableStateOf(false)
  }
  ModalBottomSheet(
    onDismissRequest = {isSheetOpen = false},
    sheetState = sheetState
  ) {

  }
  Column(
    modifier
      .fillMaxSize()
      .background(color = Color.White),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {

    Text(text = "Music Player $musicId")
  }
}
package com.example.rava.presentation.home.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rava.domain.model.MusicFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
  modifier: Modifier = Modifier,
  onDismiss: () -> Unit,
  sheetState: SheetState,
  musicFile: MusicFile
) {
  ModalBottomSheet(
    modifier = Modifier.fillMaxHeight(),
    onDismissRequest = { onDismiss() },
    sheetState = sheetState,
    scrimColor = Color.Transparent,
    dragHandle = { false },
    shape = RoundedCornerShape(0.dp)
  ) {
    Box(modifier.fillMaxSize()) {
      Text(text = musicFile?.title.toString())
    }
  }
}
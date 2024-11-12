package com.example.rava.presentation.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rava.domain.model.MusicFile

@Composable
fun MusicCard(
  modifier: Modifier = Modifier,
  musicFile: MusicFile,
  isOpen: () -> Unit,
) {
  Row(
    modifier
      .fillMaxWidth()
      .height(60.dp)
      .background(Color.Transparent)
      .clickable(
        onClick = {
          isOpen()
        }
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start
  ) {
    AsyncImage(
      model = musicFile.albumArtUri,
      contentDescription = "MusicUri",
      modifier
        .size(60.dp)
        .padding(4.dp)
        .clip(RoundedCornerShape(4.dp))
    )
    Text(text = musicFile.duration.toString(), color = Color.Black)
  }
}
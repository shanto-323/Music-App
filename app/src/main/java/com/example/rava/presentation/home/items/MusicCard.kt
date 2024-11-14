package com.example.rava.presentation.home.items

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rava.domain.model.MusicFile

@Composable
fun MusicCard(
  modifier: Modifier = Modifier,
  musicFile: MusicFile,
  isOpen: () -> Unit,
) {
  val name = musicFile.title
  val artist = musicFile.artist
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
    Column(
      modifier.weight(1f)
        .padding(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = name,
        modifier = modifier.fillMaxSize(),
        style = TextStyle(

        )
      )
    }
    IconButtonClick(imageVector = Icons.Outlined.FavoriteBorder, size = 32.dp)
  }
}
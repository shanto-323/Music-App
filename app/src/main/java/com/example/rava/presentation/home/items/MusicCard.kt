package com.example.rava.presentation.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rava.domain.model.MusicFile
import com.example.rava.presentation.home.statechange.State

@Composable
fun MusicCard(
  modifier: Modifier = Modifier,
  music: MusicFile,
  isOpen: () -> Unit,
) {
  val musicName = music.title
  val musicArtist = music.artist
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(60.dp)
      .background(Color.Transparent)
      .clickable(
        indication = null,
        interactionSource = remember {
          MutableInteractionSource()
        }
      ) {
        isOpen()
      },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    Box(
      modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .padding(2.dp)
    ) {
      AsyncImage(
        model = music.albumArtUri,
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(7.dp))
      )
    }

    Column(
      modifier = Modifier
        .fillMaxHeight()
        .weight(1f)
        .padding(horizontal = 10.dp),
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = musicName,
        style = TextStyle(
          color = Color.Black,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        ),
        maxLines = 1
      )
      Text(
        text = musicArtist,
        style = TextStyle(
          color = Color.Black,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        ),
        maxLines = 1
      )
    }
    IconButtonClick(
      imageVector = Icons.Rounded.MoreVert,
      onClick = {

      },
      tint = Color.Black,
      size = 30.dp
    )
    Spacer(modifier = Modifier.padding(5.dp))
  }

}
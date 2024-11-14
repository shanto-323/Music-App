package com.example.rava.presentation.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    Column(
      modifier
        .background(Color.DarkGray)
        .fillMaxSize()
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
    ) {
      Row(
        modifier
          .fillMaxWidth()
          .fillMaxHeight(0.2f)
          .padding(0.dp, 20.dp)
      ) {
        IconButtonClick(
          imageVector = Icons.Default.ArrowDropDown,
          onClick = {
            onDismiss()
          }
        )
      }

      if (musicFile != null) {
        Card(
          modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .weight(1f),
          elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 20.dp
          )
        ) {
          AsyncImage(
            model = musicFile.albumArtUri,
            contentDescription = "Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
          )
        }
      }

      Text(
        modifier = Modifier.fillMaxWidth(),
        text = musicFile.title,
        style = TextStyle()
      )
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = musicFile.artist,
        style = TextStyle()
      )
      MusicSlider()
      Row(
        modifier
          .fillMaxWidth()
          .fillMaxHeight(0.2f)
          .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {

        IconButtonClick(
          imageVector = Icons.Default.SkipPrevious,
          onClick = {}
        )
        IconButtonClick(
          imageVector = Icons.Default.PlayArrow,
          onClick = {},
        )
        IconButtonClick(
          imageVector = Icons.Default.SkipNext,
          onClick = {}
        )
      }
    }
  }
}


@Composable
fun IconButtonClick(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  imageVector: ImageVector,
  tint: Color = Color.White,
  size: Dp = 44.dp
) {
  IconButton(onClick = { onClick() }) {
    Icon(
      imageVector = imageVector,
      contentDescription = "",
      modifier = Modifier
        .then(modifier)
        .size(size),
      tint = tint
    )
  }
}

@Composable
fun MyText(modifier: Modifier = Modifier) {

}


@Composable
private fun MusicSlider() {
  var sliderPosition by rememberSaveable {
    mutableFloatStateOf(0f)
  }
  Slider(
    value = sliderPosition,
    onValueChange = {sliderPosition = it},
    valueRange = 0f..30f
  )
}
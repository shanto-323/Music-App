package com.example.rava.presentation.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rava.domain.model.MusicFile
import com.example.rava.presentation.home.StateChange.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
  modifier: Modifier = Modifier,
  onDismiss: () -> Unit,
  sheetState: SheetState,
  musicFile: MusicFile,
  playPause: () -> Unit,
  state: State,
  seekTo: (Long) -> Unit,
) {
  var currentPosition = state.timeOfMusic
  val audioDuration = musicFile.duration


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

      // Starting Row
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

      // Image of the content
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

      //Name && Title
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

      //Music time
      Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = formatTime(timeInMillis = currentPosition.toFloat()),
          style = TextStyle()
        )
        Text(
          text = formatTime(timeInMillis = audioDuration.toFloat()),
          style = TextStyle()
        )
      }

      //Slider
      MusicSlider(
        position = currentPosition.toFloat(),
        valueRange = musicFile.duration.toFloat(),
        newPosition = {
          seekTo(it.toLong())
        }
      )

      //Play , Skip prev , Skip next
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
          onClick = {

          }
        )
        IconButtonClick(
          imageVector = state.playPauseIcon,
          onClick = {
            playPause()
          },
        )
        IconButtonClick(
          imageVector = Icons.Default.SkipNext,
          onClick = {}
        )
      }

    }
  }
}


fun formatTime(timeInMillis: Float): String {
  val totalSeconds = (timeInMillis / 1000).toInt() // Convert milliseconds to seconds
  val minutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  return String.format("%02d:%02d", minutes, seconds)
}
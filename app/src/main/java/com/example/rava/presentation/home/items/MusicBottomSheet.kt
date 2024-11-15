package com.example.rava.presentation.home.items
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Pause
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.rava.domain.model.MusicFile
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
  modifier: Modifier = Modifier,
  onDismiss: () -> Unit,
  sheetState: SheetState,
  musicFile: MusicFile,
  isPlaying: Boolean = false,
  playPause: () -> Unit,
  player: ExoPlayer
) {
  var currentPosition by rememberSaveable { mutableStateOf(0L) }
  val audioDuration = musicFile.duration
  LaunchedEffect(player) {
    while (true) {
      currentPosition = player.currentPosition
      delay(5L)
    }
  }


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
          player.seekTo(it.toLong())
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
          imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
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


@Composable
fun IconButtonClick(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  imageVector: ImageVector,
  tint: Color = Color.White,
  size: Dp = 44.dp
) {
  IconButton(
    onClick = { onClick() },
    interactionSource = remember { MutableInteractionSource() },
  ) {
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
private fun MusicSlider(
  position: Float = 0f,
  valueRange: Float = 30f,
  newPosition: (Float) -> Unit = {}
) {

  var sliderPosition by rememberSaveable {
    mutableFloatStateOf(position)
  }
  LaunchedEffect(position) {
    sliderPosition = position
  }
  Slider(
    value = sliderPosition,
    onValueChange = {
      sliderPosition = it
      newPosition(it)
    },
    valueRange = 0f..valueRange
  )
}

fun formatTime(timeInMillis: Float): String {
  val totalSeconds = (timeInMillis / 1000).toInt() // Convert milliseconds to seconds
  val minutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  return String.format("%02d:%02d", minutes, seconds)
}
package com.example.rava.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rava.presentation.home.items.IconButtonClick
import com.example.rava.presentation.home.items.MusicBottomSheet
import com.example.rava.presentation.home.items.MusicLazyColumn
import kotlinx.coroutines.launch
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.rava.presentation.home.items.BottomNavigationBar
import com.example.rava.presentation.home.statechange.Event
import com.example.rava.presentation.home.statechange.State
import com.example.rava.presentation.home.items.formatTime
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
  navController: NavController
) {
  val state = viewModel.state
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  val exoPlayer = viewModel.exoPlayer

  var sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var isSheetOpen by rememberSaveable { mutableStateOf(false) }

  var musicFiles = state.musicFiles.collectAsLazyPagingItems()
  var music = state.music
  var isPlaying = state.isPlaying

  //Asking user permission in first time
  val permissionState = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { isGranted ->
      if (isGranted) {
        viewModel.insertMusicFiles(context)
        viewModel.getMusic()
      }
    }
  )
  LaunchedEffect(Unit) {
    if (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      viewModel.insertMusicFiles(context)
    } else {
      permissionState.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
  }


  LaunchedEffect(state) {
    music = state.music
    isPlaying = state.isPlaying
  }

  if (music != null) {
    LaunchedEffect(isPlaying) {
      if (isPlaying)
        exoPlayer.play()
      else exoPlayer.pause()
      if (!isPlaying) viewModel.onEvent(Event.PlayPauseIcon(playPauseIcon = Icons.Rounded.PlayArrow))
      else viewModel.onEvent(Event.PlayPauseIcon(playPauseIcon = Icons.Rounded.Pause))
    }
    LaunchedEffect(music) {
      Log.d("TAG2","${musicFiles.itemSnapshotList.size}")
      viewModel.exoplayerInstance(music.path)
    }
    LaunchedEffect(exoPlayer) {
      while (true) {
        viewModel.onEvent(Event.TimeOfMusic(timeOfMusic = exoPlayer.currentPosition))
        delay(5L)
      }
    }
    LaunchedEffect(Unit) {
      if (state.timeOfMusic > 0L) {
        exoPlayer.seekTo(state.timeOfMusic)
      }
    }
  }




  Box(
    modifier
      .background(Color.DarkGray)
      .fillMaxSize()
      .statusBarsPadding()
  ) {
    MusicLazyColumn(
      onClick = {
        isSheetOpen = true
        scope.launch {
          sheetState.show()
        }
        if (music != null) {
          viewModel.onEvent(Event.IsPlaying(isPlaying = true))
        }
      },
      musicFiles = musicFiles,
      musicPlay = { music ->
        viewModel.saveState(music = music)
      },
      modifier = Modifier.padding(10.dp),
      viewModel = viewModel
    )

    Box(modifier = modifier.align(Alignment.BottomCenter)) {
      BottomNavigationBar()
    }

    if (music != null) {
      Card(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .offset(y = (-130).dp)
          .fillMaxWidth(0.9f)
          .fillMaxHeight(0.07f),
      ) {
        FloatingCardButton(
          state = state,
          playPause = {
            viewModel.onEvent(Event.IsPlaying(isPlaying = !state.isPlaying))
          },
          fullClick = {
            isSheetOpen = true
            scope.launch {
              sheetState.show()
            }
          }
        )
      }
    }
  }

  if (isSheetOpen && music != null) {
    MusicBottomSheet(
      onDismiss = { isSheetOpen = false },
      sheetState = sheetState,
      playPause = {
        viewModel.onEvent(Event.IsPlaying(isPlaying = !isPlaying))
      },
      seekTo = {
        exoPlayer.seekTo(it)
      },
      state = state
    )
  }

}


@Composable
fun FloatingCardButton(
  modifier: Modifier = Modifier,
  playPause: () -> Unit = {},
  fullClick: () -> Unit = {},
  state: State = State()
) {
  val music = state.music
  val musicName = music.title
  val musicArtist = music.artist
  val timeRatio = state.timeOfMusic.toFloat() / music.duration.toFloat()
  val color = lerp(Color.Black, Color.Red, timeRatio.coerceIn(0f, 1f))

  Row(
    modifier = modifier
      .fillMaxSize()
      .clip(RoundedCornerShape(6.dp))
      .background(Color.White)
      .padding(10.dp)
      .clickable(
        indication = null,
        interactionSource = remember {
          MutableInteractionSource()
        }
      ) {
        fullClick()
      },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    Box(
      modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .border(1.dp, color, RoundedCornerShape(8.dp))
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
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        ),
        maxLines = 1
      )
      Text(
        text = musicArtist,
        style = TextStyle(
          color = Color.Black,
          fontSize = 8.sp,
          fontWeight = FontWeight.SemiBold
        )
      )
      if (state.timeOfMusic > 0) {
        Text(
          text = formatTime(state.timeOfMusic.toFloat()),
          style = TextStyle(
            color = Color.Black,
            fontSize = 8.sp,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }
    Spacer(modifier = modifier.padding(10.dp))
    IconButtonClick(
      imageVector = Icons.Rounded.FavoriteBorder,
      onClick = {

      },
      tint = Color.Black,
      size = 50.dp
    )
    IconButtonClick(
      imageVector = state.playPauseIcon,
      onClick = {
        playPause()
      },
      tint = Color.Black,
      size = 60.dp
    )
  }
}


@Preview
@Composable
private fun Preview() {
  FloatingCardButton()
}



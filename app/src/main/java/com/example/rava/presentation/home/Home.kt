package com.example.rava.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import com.example.rava.presentation.home.StateChange.Event
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
  var musicPlay = state.music
  var isPlaying = state.isPlaying
  var playPauseIcon = state.playPauseIcon

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
    musicPlay = state.music
    isPlaying = state.isPlaying
    playPauseIcon = state.playPauseIcon
    if (!isPlaying) viewModel.onEvent(Event.PlayPauseIcon(playPauseIcon = Icons.Default.PlayArrow))
    else viewModel.onEvent(Event.PlayPauseIcon(playPauseIcon = Icons.Default.Pause))
  }

  if (musicPlay != null) {
    LaunchedEffect(isPlaying) {
      if (isPlaying)
        exoPlayer.play()
      else exoPlayer.pause()
    }
    LaunchedEffect(musicPlay) {
      viewModel.exoplayerInstance(musicPlay.path)
    }
    LaunchedEffect(exoPlayer) {
      while (true) {
        viewModel.onEvent(Event.TimeOfMusic(timeOfMusic = exoPlayer.currentPosition))
        delay(5L)
      }
    }
    LaunchedEffect(Unit) {
      Log.d("TAG2", "Seek to ${state.timeOfMusic}")
      if (state.timeOfMusic > 0L) {
        Log.d("TAG2", "Seek to @ ${state.timeOfMusic}")
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
        if (musicPlay != null) {
          viewModel.onEvent(Event.IsPlaying(isPlaying = true))
        }
      },
      musicFiles = musicFiles,
      musicPlay = { music ->
        viewModel.saveState(music = music)
      },
      modifier = Modifier.padding(10.dp)
    )

    Box(modifier = modifier.align(Alignment.BottomCenter)) {
      BottomNavigationBar()
    }

    FloatingActionButton(
      onClick = { /* Your FAB action */ },
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .offset(y = (-130).dp)
        .fillMaxWidth(0.9f)
        .fillMaxHeight(0.07f),
      containerColor = Color.Black
    ) {
      Row(
        modifier = modifier.padding(10.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clickable(
              indication = null,
              interactionSource = remember {
                MutableInteractionSource()
              }
            ) {
              isSheetOpen = true
              scope.launch {
                sheetState.show()
              }
            }
        ) {
          AsyncImage(
            model = musicPlay?.albumArtUri,
            contentDescription = "Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
              .fillMaxHeight()
              .aspectRatio(1f)
              .clip(RoundedCornerShape(10.dp))
          )
        }
        IconButtonClick(
          imageVector = playPauseIcon,
          onClick = {
            if (isPlaying) {
              exoPlayer.pause()
              viewModel.onEvent(Event.PlayPauseIcon(playPauseIcon = Icons.Default.Pause))
            } else {
              exoPlayer.play()
              viewModel.onEvent(Event.PlayPauseIcon(playPauseIcon = Icons.Default.PlayArrow))
            }
            viewModel.onEvent(Event.IsPlaying(isPlaying = !state.isPlaying))
          }
        )

      }
    }
  }

  if (isSheetOpen && musicPlay != null) {
    MusicBottomSheet(
      onDismiss = { isSheetOpen = false },
      sheetState = sheetState,
      musicFile = musicPlay!!,
      playPause = {
        if (isPlaying) {
          exoPlayer.pause()
        } else {
          exoPlayer.play()
        }
        viewModel.onEvent(Event.IsPlaying(isPlaying = !isPlaying))
      },
      state = state,
      seekTo = {
        exoPlayer.seekTo(it)
        Log.d("TAG2", "Seek To: $it")
      }
    )
  }

}








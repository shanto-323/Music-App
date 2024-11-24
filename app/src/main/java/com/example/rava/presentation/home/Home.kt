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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.rava.presentation.home.items.Event
import kotlinx.coroutines.delay


/*
1. problem with configuration change
2. add a search bar
3. modify play pause
*/



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

  //-----------------------------------------------

  LaunchedEffect(state.isPlaying) {
    exoPlayer.playWhenReady = state.isPlaying
    if (!state.isPlaying){
      exoPlayer.pause()
    }
    Log.d("TAG2", "Home: ${state.isPlaying}")
  }

  if (musicPlay != null) {
    LaunchedEffect(Unit) {
      viewModel.restoreCurrentMusic()
    }
  }
  LaunchedEffect(musicPlay) {
    viewModel.exoplayerInstance(musicPlay.path)
    exoPlayer.playWhenReady = true
  }
  //Get last playback position
  LaunchedEffect(Unit) {
    if (state.timeOfMusic > 0L) {
      exoPlayer.seekTo(state.timeOfMusic)
    }
  }

  //Update playback position in every 5mil sec
  LaunchedEffect(exoPlayer) {
    while (true) {
      viewModel.onEvent(Event.TimeOfMusic(timeOfMusic = exoPlayer.currentPosition))
      delay(5L)
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
          imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
          onClick = {
            if (isPlaying) {
              exoPlayer.pause()
            } else {
              exoPlayer.play()
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
      isPlaying = isPlaying,
      playPause = {
        if (isPlaying) {
          exoPlayer.pause()
        } else {
          exoPlayer.play()
        }
        viewModel.onEvent(Event.IsPlaying(isPlaying = !isPlaying))
      },
      player = exoPlayer,
      viewModel = viewModel,
      state = state
    )
  }

}


@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
  val navList = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Library
  )
  NavigationBar(
    modifier = modifier
      .background(Color.Black.copy(alpha = 0.8f))
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    containerColor = Color.Transparent,
  ) {
    navList.forEach { item ->
      NavigationBarItem(selected = false, onClick = { /*TODO*/ },
        icon = {
          Icon(
            imageVector = item.icon,
            contentDescription = "",
            tint = Color.White,
            modifier = modifier.size(32.dp)
          )
        }
      )
    }
  }

}


sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
  data object Home : Screen("home", Icons.Default.Home, "Home")
  data object Search : Screen("search", Icons.Default.Search, "Search")
  data object Library : Screen("library", Icons.Default.LibraryMusic, "Library")
}





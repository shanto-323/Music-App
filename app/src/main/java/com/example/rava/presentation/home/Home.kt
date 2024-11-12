package com.example.rava.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rava.domain.model.MusicFile
import com.example.rava.presentation.home.items.MusicBottomSheet
import com.example.rava.presentation.home.items.MusicCard
import com.example.rava.presentation.home.items.MusicLazyColumn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
  navController: NavController
) {

  var sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )
  var isSheetOpen by rememberSaveable {
    mutableStateOf(false)
  }
  var musicPlay by rememberSaveable {
    mutableStateOf<MusicFile?>(null)
  }
  LaunchedEffect(musicPlay) {
    Log.d("TAG2", "Home: $musicPlay")
  }

  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  var musicFiles by rememberSaveable {
    mutableStateOf(emptyList<MusicFile>())
  }
  val permissionState = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { isGranted ->
      if (isGranted) {
        viewModel.getMusic(context)
        musicFiles = viewModel.state.musicFiles
      }
    }
  )
  LaunchedEffect(Unit) {
    if (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      viewModel.getMusic(context)
      musicFiles = viewModel.state.musicFiles
    } else {
      permissionState.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
  }


  Scaffold(
    content = {
      Column(
        modifier
          .padding(it)
          .fillMaxSize()
          .background(Color.Black)
          .imePadding()
      ) {
        Box(
          modifier
            .fillMaxHeight(0.08f)
            .fillMaxWidth()
            .padding(1.dp, 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
            .background(Color.Black)
        ) {
          //This is for the Search Bar
        }

        Row {
          NavigationRail(
            modifier
              .fillMaxWidth(0.2f)
              .fillMaxHeight()
              .padding(1.dp, 2.dp)
              .clip(RoundedCornerShape(4.dp)),
            containerColor = Color.Black,
            contentColor = Color.White,
          ) {
            Column(
              modifier.fillMaxSize(),
              verticalArrangement = Arrangement.Bottom,
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              RailItem()
              RailItem()
              RailItem()
              RailItem()
            }
          }

          Column(
            modifier.weight(1f)
          ) {
            Column(
              modifier
                .fillMaxSize()
                .padding(1.dp, 0.dp, 1.dp, 2.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                .background(Color.White)
            ) {
              MusicLazyColumn(
                onClick = {
                  isSheetOpen = true
                  scope.launch {
                    sheetState.show()
                  }
                },
                musicFiles = musicFiles,
                musicPlay = { music ->
                  musicPlay = music
                }
              )
            }

          }
        }

        if (isSheetOpen && musicPlay != null) {
          MusicBottomSheet(onDismiss = { isSheetOpen = false }, sheetState = sheetState, musicFile = musicPlay!!)
        }

      }
    }
  )
}

@Composable
fun RailItem(
  modifier: Modifier = Modifier
) {
  Column(
    modifier
      .fillMaxWidth()
      .aspectRatio(0.8f)
      .padding(2.dp)//This is just for adjustment
    ,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(imageVector = Icons.Default.Home, contentDescription = "HOME")
    Text(text = "Home")
  }
}





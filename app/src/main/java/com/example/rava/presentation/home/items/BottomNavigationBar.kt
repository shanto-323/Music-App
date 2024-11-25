package com.example.rava.presentation.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

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
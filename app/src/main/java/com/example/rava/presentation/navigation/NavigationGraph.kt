package com.example.rava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rava.presentation.home.Home
import com.example.rava.presentation.player.MusicPlayer

@Composable
fun Navigation(modifier: Modifier = Modifier) {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Route.HOME) {
    composable(route = Route.HOME) {
      Home(
        navController = navController
      )
    }
    composable(
      route = "${Route.PLAYER}/{id}",
      arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
      val id = backStackEntry.arguments?.getInt("id")
      id?.let {
        MusicPlayer(
          musicId = it
        )
      }
    }
  }
}
package com.example.rava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rava.presentation.home.Home

@Composable
fun Navigation(modifier: Modifier = Modifier) {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Route.HOME) {
    composable(route = Route.HOME) {
      Home(
        navController = navController
      )
    }
  }
}
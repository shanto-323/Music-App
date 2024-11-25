package com.example.rava.presentation.home.items

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun MusicSlider(
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
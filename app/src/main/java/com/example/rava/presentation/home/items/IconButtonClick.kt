package com.example.rava.presentation.home.items

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

package com.example.rava.presentation.home.items

import androidx.paging.PagingData
import com.example.rava.domain.model.MusicFile

data class State(
  val musicFiles: PagingData<MusicFile> = PagingData.empty()
)
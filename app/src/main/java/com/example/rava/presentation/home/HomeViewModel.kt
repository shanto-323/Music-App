package com.example.rava.presentation.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rava.domain.repository.Repository
import com.example.rava.presentation.home.items.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: Repository
) : ViewModel(){
  var state by mutableStateOf(State())
    private set

  fun getMusic(context: Context){
    state = state.copy(
      musicFiles = repository.getMusicFiles(context)
    )
  }
}
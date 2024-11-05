package com.example.rava.domain.repository

import android.content.Context
import com.example.rava.domain.model.MusicFile

interface Repository {
  fun getMusicFiles(context: Context): List<MusicFile>
}
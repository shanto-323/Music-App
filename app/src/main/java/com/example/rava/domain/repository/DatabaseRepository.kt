package com.example.rava.domain.repository

import android.content.Context
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.rava.data.database.model.MusicEntity
import com.example.rava.domain.model.MusicFile
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
  fun getAllMusic(): Flow<PagingData<MusicFile>>
  suspend fun insertMusicFiles(context: Context)
}
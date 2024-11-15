package com.example.rava.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.example.rava.data.dao.Dao
import com.example.rava.data.database.model.MusicEntity
import com.example.rava.domain.mapper.toDomainModel
import com.example.rava.domain.model.MusicFile
import com.example.rava.domain.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class DatabaseRepositoryImpl(
  private val dao: Dao
) : DatabaseRepository {

  override fun getAllMusic(): Flow<PagingData<MusicFile>> {
    return Pager(
      config = PagingConfig(
        pageSize = 10,
        prefetchDistance = 20,
        enablePlaceholders = false
      ),
      pagingSourceFactory = { dao.getAllMusic() }
    ).flow.map { pagingData ->
      pagingData.map { entity -> entity.toDomainModel() }
    }
  }

  override suspend fun insertMusicFiles(context: Context) {
    val musicFiles = mutableListOf<MusicEntity>()

    withContext(Dispatchers.IO) {
      val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DURATION
      )
      val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

      val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        null
      )

      cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

        while (it.moveToNext()) {
          val id = it.getLong(idColumn)
          val title = it.getString(titleColumn)
          val artist = it.getString(artistColumn)
          val path = it.getString(dataColumn)
          val duration = it.getLong(durationColumn)
          val albumId = it.getLong(albumIdColumn)

          val albumArtUri = ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
          )

          if (duration >= 90000) {
            musicFiles.add(
              MusicEntity(
                id = id.toInt(),
                title = title,
                artist = artist,
                path = path,
                duration = duration,
                albumArtUri = albumArtUri.toString()
              )
            )
          }
        }
      }
      if (musicFiles.isNotEmpty()) {
        dao.insertAllMusic(musicFiles)
      }

    }

  }

}
package com.example.rava.presentation.home

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun fetchLocalMusic(context: Context): List<MusicFile> {
  val musicFiles = mutableListOf<MusicFile>()

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
        musicFiles.add(MusicFile(id, title, artist, path, duration, albumArtUri))
      }
    }
  }

  return musicFiles
}


data class MusicFile(
  val id: Long,
  val title: String,
  val artist: String,
  val path: String,
  val duration: Long,
  val albumArtUri: Uri
)


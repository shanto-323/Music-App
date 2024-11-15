package com.example.rava.domain.mapper

import android.net.Uri
import com.example.rava.data.database.model.MusicEntity
import com.example.rava.domain.model.MusicFile

fun MusicEntity.toDomainModel(): MusicFile {
  return MusicFile(
    id = this.id.toLong(),
    title = this.title,
    artist = this.artist,
    path = this.path,
    duration = this.duration,
    albumArtUri = Uri.parse(this.albumArtUri)
  )
}
fun MusicFile.toDomainModel(): MusicEntity {
  return MusicEntity(
    id = this.id.toInt(),
    title = this.title,
    artist = this.artist,
    path = this.path,
    duration = this.duration,
    albumArtUri = this.albumArtUri.toString()
  )
}
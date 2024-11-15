package com.example.rava.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.rava.data.dao.Dao
import com.example.rava.data.database.MusicDatabase
import com.example.rava.data.repository.DatabaseRepositoryImpl
import com.example.rava.data.repository.RepositoryImpl
import com.example.rava.domain.repository.DatabaseRepository
import com.example.rava.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideExoplayer(
    @ApplicationContext context: Context
  ): ExoPlayer {
    return ExoPlayer.Builder(context).build()
  }

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext app: Context): MusicDatabase {
    return Room.databaseBuilder(
      app,
      MusicDatabase::class.java,
      "music_db"
    ).build()
  }

  @Provides
  @Singleton
  fun provideDao(db: MusicDatabase): Dao {
    return db.musicDao()
  }

  @Provides
  @Singleton
  fun provideMusicRepository(dao: Dao): DatabaseRepository {
    return DatabaseRepositoryImpl(dao)
  }

}
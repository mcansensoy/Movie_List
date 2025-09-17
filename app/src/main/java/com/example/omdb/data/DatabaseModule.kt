package com.example.omdb.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): MovieDatabase =
        Room.databaseBuilder(app, MovieDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration() // eski veriyi sil, yeni tabloyu kur
            .build()

    @Provides
    @Singleton
    fun provideMovieDao(db: MovieDatabase): MovieDao = db.movieDao()

    @Provides
    @Singleton
    fun provideWatchlistDao(db: MovieDatabase): WatchlistDao = db.watchlistDao()
}

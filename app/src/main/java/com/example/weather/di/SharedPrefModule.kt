package com.example.weather.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPrefModule {

  @Singleton
  @Provides
  fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
    return context.getSharedPreferences("preferences_file", Context.MODE_PRIVATE)
  }
}
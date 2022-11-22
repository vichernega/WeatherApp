package com.example.weather.di

import com.example.weather.network.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WeatherApiModule {

  private val baseUrl = "https://api.openweathermap.org/data/2.5/"

  @Provides
  @Singleton
  fun provideAuthInterceptorOkHttpClient(): OkHttpClient {
    return OkHttpClient
      .Builder()
      .build()
  }

  @Provides
  @Singleton
  fun provideWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)

      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(baseUrl)
      .build()
  }

  @Provides
  @Singleton
  fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
    return retrofit.create(WeatherApiService::class.java)
  }
}
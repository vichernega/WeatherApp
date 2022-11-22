package com.example.weather.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiClient {

  private lateinit var retrofit: Retrofit

  fun getClient(baseUrl: String): Retrofit {
    if (!this::retrofit.isInitialized) {
      retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
    return retrofit
  }
}
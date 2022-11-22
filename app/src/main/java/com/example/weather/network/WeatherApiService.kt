package com.example.weather.network

import com.example.weather.BuildConfig
import com.example.weather.data.entity.CurrentWeatherDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

  @GET("weather?")
  suspend fun getCurrentWeather(
    @Query("lat") latitude: String = "45.4371908",
    @Query("lon") longitude: String = "12.3345898",
    @Query("units") units: String = "metric",
    @Query("apiKey") apiKey: String = BuildConfig.WEATHER_API_KEY,
  ): CurrentWeatherDto
}
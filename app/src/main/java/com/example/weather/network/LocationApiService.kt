package com.example.weather.network

import com.example.weather.BuildConfig
import com.example.weather.data.entity.location.LocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {

  @GET("geo/1.0/direct?")
  suspend fun getCoordinates(
    @Query("q") cityName: String,
    @Query("apiKey") apiKey: String = BuildConfig.WEATHER_API_KEY,
  ): List<LocationDto>
}
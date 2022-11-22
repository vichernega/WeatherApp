package com.example.weather.repository

import com.example.weather.data.entity.CurrentWeatherDto
import com.example.weather.data.model.Coordinates
import com.example.weather.network.WeatherApiService
import com.example.weather.state.RequestState
import javax.inject.Inject

class WeatherRepository @Inject constructor(
  private val weatherApiService: WeatherApiService
) {

  suspend fun loadCurrentWeather(coordinates: Coordinates): RequestState<CurrentWeatherDto> {
    return try {
      val currentWeatherResponse = weatherApiService.getCurrentWeather(
        longitude = coordinates.longitude.toString(),
        latitude = coordinates.latitude.toString()
      )
      RequestState.Success(currentWeatherResponse)
    } catch (e: Exception) {
      RequestState.Error(e)
    }
  }
}
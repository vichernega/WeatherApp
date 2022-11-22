package com.example.weather.repository

import com.example.weather.data.entity.CurrentWeatherDto
import com.example.weather.network.WeatherApiService
import com.example.weather.state.RequestState
import javax.inject.Inject

class WeatherRepository @Inject constructor(
  private val weatherApiService: WeatherApiService
) {

  suspend fun loadCurrentWeather(): RequestState<CurrentWeatherDto> {
    return try {
      val currentWeatherResponse = weatherApiService.getCurrentWeather()
      RequestState.Success(currentWeatherResponse)
    } catch (e: Exception) {
      RequestState.Error()
    }
  }
}
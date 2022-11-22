package com.example.weather.data.model

data class CurrentWeather(
  val coordinates: Coordinates?,
  val weather: Weather?,
  val base: String?,
  val weatherParameters: WeatherParameters?,
  val visibility: Int?,
  val wind: Wind?,
  val precipitations: Precipitations?,
  val clouds: Clouds?,
  val dataTime: Long?,
  val timezone: Int?,
  val id: Int?,
  val town: String?,
  val code: Int?,
)
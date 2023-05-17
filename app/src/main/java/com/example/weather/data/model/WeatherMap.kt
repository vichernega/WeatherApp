package com.example.weather.data.model

data class WeatherMap(
  val layer: String,
  val name: String,
  val iconResource: Int,
  var link: String = ""
)

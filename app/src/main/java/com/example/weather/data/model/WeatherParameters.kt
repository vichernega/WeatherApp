package com.example.weather.data.model

import kotlin.math.roundToInt

data class WeatherParameters(
  val temperature: String,
  val feelsLikeTemperature: String,
  val minTemperature: String,
  val maxTemperature: String,
  val pressure: Int,
  val humidity: Int,
  val seaLevelPressure: Int,
  val groundLevelPressure: Int,
  val isMinValid: Boolean,
  val isMaxValid: Boolean,
)

fun formatStringToTemperature(raw: Double?) = raw?.let { "${raw.toFloat().roundToInt()}°" } ?: ""

fun isMaxTemperatureValid(max: Double, current: Double) = max >= current

fun isMinTemperatureValid(min: Double, current: Double) = min <= current

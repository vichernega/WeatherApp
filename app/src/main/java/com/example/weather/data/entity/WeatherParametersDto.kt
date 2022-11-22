package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherParametersDto(
  @SerializedName("temp") val temperature: Double,
  @SerializedName("feels_like") val feelsLikeTemperature: Double,
  @SerializedName("temp_min") val minTemperature: Double,
  @SerializedName("temp_max") val maxTemperature: Double,

  /** [pressure] - hPa */
  @SerializedName("pressure") val pressure: Int,

  /** [humidity] - percentage */
  @SerializedName("humidity") val humidity: Int,

  /** [seaLevelPressure] - Atmospheric pressure on the sea level, hPa */
  @SerializedName("sea_level") val seaLevelPressure: Int,

  /** [groundLevelPressure] - Atmospheric pressure on the ground level, hPa */
  @SerializedName("grnd_level") val groundLevelPressure: Int,
)

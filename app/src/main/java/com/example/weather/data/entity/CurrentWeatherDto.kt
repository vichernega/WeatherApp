package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
  @SerializedName("coord") val coordinatesDto: CoordinatesDto?,
  @SerializedName("weather") val weatherDto: List<WeatherDto>?,
  @SerializedName("base") val base: String?,
  @SerializedName("main") val weatherParametersDto: WeatherParametersDto?,
  @SerializedName("visibility") val visibility: Int?,
  @SerializedName("wind") val windDto: WindDto?,
  @SerializedName("rain") val rainDto: RainDto?,
  @SerializedName("snow") val snowDto: SnowDto?,
  @SerializedName("clouds") val cloudsDto: CloudsDto?,
  @SerializedName("dt") val dataTime: Long?,
  @SerializedName("timezone") val timezone: Int?,
  @SerializedName("id") val id: Int?,
  @SerializedName("name") val name: String?,
  @SerializedName("cod") val code: Int?,
)
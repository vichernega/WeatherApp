package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WindDto(
  /** [speed] - Wind speed. Unit Default: meter/sec */
  @SerializedName("speed") val speed: Double,

  /** [direction] - Wind direction, degrees */
  @SerializedName("deg") val direction: Int,

  /** [gust] - Wind gust. Unit Default: meter/sec */
  @SerializedName("gust") val gust: Double,
)
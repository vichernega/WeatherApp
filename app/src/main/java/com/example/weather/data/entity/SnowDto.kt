package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SnowDto(
  /** [lastHour] - Snow volume for the last 1 hour, mm */
  @SerializedName("1h") val lastHour: Double,
  /** [lastThreeHours] - Snow volume for the last 3 hours, mm */
  @SerializedName("3h") val lastThreeHours: Double
)

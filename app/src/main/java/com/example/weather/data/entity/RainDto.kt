package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RainDto(
  /** [lastHour] - Rain volume for the last 1 hour, mm */
  @SerializedName("1h") val lastHour: Double?,
  /** [lastThreeHours] - Rain volume for the last 3 hours, mm */
  @SerializedName("3h") val lastThreeHours: Double?
)

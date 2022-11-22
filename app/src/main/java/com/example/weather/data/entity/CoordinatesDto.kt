package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesDto(
  @SerializedName("lon") val longitude: Double,
  @SerializedName("lat") val latitude: Double
)

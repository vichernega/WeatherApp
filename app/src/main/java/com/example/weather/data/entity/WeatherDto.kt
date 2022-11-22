package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
  @SerializedName("id") val id: Int,
  @SerializedName("main") val title: String,
  @SerializedName("description") val description: String,
  @SerializedName("icon") val icon: String
)

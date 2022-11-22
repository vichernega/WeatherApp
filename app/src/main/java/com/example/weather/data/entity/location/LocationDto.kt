package com.example.weather.data.entity.location

import com.google.gson.annotations.SerializedName

data class LocationDto(
  @SerializedName("name") val cityName: String?,
  @SerializedName("lon") val longitude: Double?,
  @SerializedName("lat") val latitude: Double?,
  @SerializedName("country") val country: String?,
  @SerializedName("state") val state: String?
)
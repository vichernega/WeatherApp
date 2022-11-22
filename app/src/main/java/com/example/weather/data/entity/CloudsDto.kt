package com.example.weather.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CloudsDto(
  /** [cloudiness] - percentage */
  @SerializedName("all") val cloudiness: Int
)

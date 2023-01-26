package com.example.weather.data.model.location

import com.example.weather.data.model.Coordinates
import kotlinx.serialization.Serializable

@Serializable
data class Location(
  val city: String,
  val coordinates: Coordinates,
  val country: String,
  val state: String?,
  var isSelected: Boolean = false,
)
package com.example.weather.data.model.location

import com.example.weather.data.model.Coordinates

data class Location(
  val city: String,
  val coordinates: Coordinates,
  val country: String,
  val state: String?
)
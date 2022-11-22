package com.example.weather.data.model

import com.example.weather.data.entity.CoordinatesDto

data class Coordinates(val longitude: Double?, val latitude: Double?) {
  companion object {
    fun mapCoordinates(coordinatesDto: CoordinatesDto): Coordinates {
      return Coordinates(
        longitude = coordinatesDto.longitude,
        latitude = coordinatesDto.latitude
      )
    }
  }
}


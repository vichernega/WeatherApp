package com.example.weather.repository

import com.example.weather.data.entity.location.LocationDto
import com.example.weather.network.LocationApiService
import com.example.weather.state.RequestState
import javax.inject.Inject

class LocationRepository @Inject constructor(
  private val locationApiService: LocationApiService
) {

  suspend fun loadCoordinates(query: String): RequestState<LocationDto> {
    return try {
      val locationResponse = locationApiService.getCoordinates(query)[0]
      RequestState.Success(locationResponse)
    } catch (e: Exception) {
      RequestState.Error(e)
    }
  }
}
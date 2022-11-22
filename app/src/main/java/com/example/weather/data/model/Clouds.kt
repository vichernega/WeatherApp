package com.example.weather.data.model

import com.example.weather.data.entity.CloudsDto

data class Clouds(val cloudiness: Int) {
  companion object {
    fun mapClouds(cloudsDto: CloudsDto): Clouds {
      return Clouds(cloudiness = cloudsDto.cloudiness)
    }
  }
}

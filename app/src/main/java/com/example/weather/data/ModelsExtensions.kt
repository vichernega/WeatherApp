package com.example.weather.data

import android.util.Log
import com.example.weather.data.entity.*
import com.example.weather.data.entity.location.LocationDto
import com.example.weather.data.model.*
import com.example.weather.data.model.Precipitations.Rain
import com.example.weather.data.model.Precipitations.Snow
import com.example.weather.data.model.location.Location

fun CurrentWeatherDto.convertToModel(): CurrentWeather {
  val precipitations = rainDto?.convertToModel() ?: snowDto?.convertToModel()
  return CurrentWeather(
    coordinates = coordinatesDto?.convertToModel(),
    weather = weatherDto?.get(0)?.convertToModel(),
    base = base,
    weatherParameters = weatherParametersDto?.convertToModel(),
    visibility = visibility,
    wind = windDto?.convertToModel(),
    precipitations = precipitations,
    clouds = cloudsDto?.convertToModel(),
    dataTime = dataTime,
    timezone = timezone,
    id = id,
    town = name,
    code = code
  )
}

fun CoordinatesDto.convertToModel(): Coordinates {
  return Coordinates(longitude = longitude, latitude = latitude)
}

fun WeatherDto.convertToModel(): Weather {
  return Weather(
    title = title,
    description = description
  )
}

fun WeatherParametersDto.convertToModel(): WeatherParameters {
  Log.d("VICH", "current = $temperature")
  Log.d("VICH", "min = $minTemperature, isValid = ${isMinTemperatureValid(minTemperature, temperature)}")
  Log.d("VICH", "max = $maxTemperature, isValid = ${isMaxTemperatureValid(maxTemperature, temperature)}")
  return WeatherParameters(
    temperature = formatStringToTemperature(temperature),
    feelsLikeTemperature = formatStringToTemperature(feelsLikeTemperature),
    minTemperature = formatStringToTemperature(minTemperature),
    maxTemperature = formatStringToTemperature(maxTemperature),
    pressure = pressure,
    humidity = humidity,
    seaLevelPressure = seaLevelPressure,
    groundLevelPressure = groundLevelPressure,
    isMinValid = isMinTemperatureValid(minTemperature, temperature),
    isMaxValid = isMaxTemperatureValid(maxTemperature, temperature)
  )
}


fun WindDto.convertToModel(): Wind {
  return Wind(
    speed = speed,
    direction = direction,
    gust = gust
  )
}

fun RainDto.convertToModel(): Rain {
  return Rain(
    lastHourVolume = lastHour,
    lastThreeHoursVolume = lastThreeHours
  )
}

fun SnowDto.convertToModel(): Snow {
  return Snow(
    lastHourVolume = lastHour,
    lastThreeHoursVolume = lastThreeHours
  )
}

fun CloudsDto.convertToModel(): Clouds {
  return Clouds(cloudiness = cloudiness)
}

// LOCATION

fun LocationDto.convertToModel(): Location {
  val receivedCoordinates = Coordinates(longitude = longitude, latitude  = latitude)
  return Location(
    city = cityName ?: "Location",
    coordinates = receivedCoordinates,
    country = country ?: " Country",
    state = state
  )
}















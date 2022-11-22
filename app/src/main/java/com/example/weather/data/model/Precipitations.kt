package com.example.weather.data.model

sealed class Precipitations(val lastHour: Double?, val lastThreeHours: Double?) {

  data class Rain(private val lastHourVolume: Double?, private val lastThreeHoursVolume: Double?):
    Precipitations(lastHourVolume, lastThreeHoursVolume)

  data class Snow(private val lastHourVolume: Double?, private val lastThreeHoursVolume: Double?):
    Precipitations(lastHourVolume, lastThreeHoursVolume)
}

package com.example.weather.navigation

import com.example.weather.R

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
  object WeatherScreenItem: BottomNavItem(TITLE_WEATHER_SCREEN, R.drawable.ic_partly_cloudy, "weather_screen")
  object SavedLocationsScreenItem: BottomNavItem(TITLE_SAVED_LOCATIONS_SCREEN, R.drawable.ic_map_marker, "saved_locations_screen")
}
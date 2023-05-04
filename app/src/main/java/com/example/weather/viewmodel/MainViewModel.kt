package com.example.weather.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.convertToModel
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.location.Location
import com.example.weather.repository.LocationRepository
import com.example.weather.repository.WeatherRepository
import com.example.weather.state.RequestState
import com.example.weather.state.UiState
import com.example.weather.utils.CURRENT_LOCATION_KEY
import com.example.weather.utils.LOCATIONS_KEY
import com.example.weather.utils.fromJson
import com.example.weather.utils.log
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val weatherRepository: WeatherRepository,
  private val locationRepository: LocationRepository,
  private val sharedPreferences: SharedPreferences,
  ): ViewModel() {

  private val gson = Gson()

  private val _currentWeatherState: MutableStateFlow<CurrentWeather?> = MutableStateFlow(null)
  val currentWeatherState: StateFlow<CurrentWeather?> = _currentWeatherState.asStateFlow()

  private val _locationListState: MutableStateFlow<List<Location>> = MutableStateFlow(mapLocationsList())
  val locationListState: StateFlow<List<Location>> = _locationListState.asStateFlow()

  // TODO - remove Boolean from UiState
  private val _loadingState: MutableStateFlow<UiState<Boolean>?> = MutableStateFlow(null)
  val loadingState: StateFlow<UiState<Boolean>?> = _loadingState.asStateFlow()

  init {
    loadCurrentWeather()
  }

  private fun loadCurrentWeather() {
    viewModelScope.launch {
      getCurrentLocation()?.let { currentLocation ->
        _loadingState.value = UiState.Loading()
        log("GET current weather STARTED")
        when (val response = weatherRepository.loadCurrentWeather(currentLocation.coordinates)) {
          is RequestState.Success -> {
            val currentWeather = response.response.copy(name = currentLocation.city).convertToModel()
            _loadingState. value = UiState.Success()
            _currentWeatherState.value = currentWeather
            log("GET current weather RESULT  model: $currentWeather")
            log("GET current weather RESULT entity: ${response.response}")
          }
          is RequestState.Error -> {
            _loadingState.value = UiState.Error()
            log("GET current weather ERROR: ${response.exception.message}")
          }
        }
      }
      log("GET current weather DONE")
    }
  }

  fun loadCoordinates(query: String) {
    viewModelScope.launch {
      _loadingState.value = UiState.Loading()
      log("GET coordinates STARTED")
      when (val response = locationRepository.loadCoordinates(query)) {
        is RequestState.Success -> {
          val location = response.response.convertToModel()
          log("GET coordinates RESULT  model: $location")
          log("GET coordinates RESULT entity: ${response.response}")

          saveToSharedPreferences(CURRENT_LOCATION_KEY, location)
          addLocationToSharedPreferences(location)

          _locationListState.value = mapLocationsList()
          _loadingState.value = UiState.Success()
          log("GET coordinates CURRENT_LOCATION: ${locationListState.value}")


          // TODO - remove (testing chunk)
          val currentLocation = sharedPreferences.getString(CURRENT_LOCATION_KEY, "currentLocation default")
          val deserializedLocation = gson.fromJson(currentLocation, Location::class.java)
          log("GET coordinates RESULT from SHARED PREFS string        CURRENT_LOCATION: $currentLocation")
          log("GET coordinates RESULT from SHARED PREFS deserialized  CURRENT_LOCATION: $deserializedLocation")
        }
        is RequestState.Error -> {
          _loadingState.value = UiState.Error()
          log("GET coordinates ERROR: ${response.exception.message}")
        }
      }
      log("GET coordinates DONE")
    }
  }

  fun selectCurrentLocation(location: Location) {
    saveToSharedPreferences(CURRENT_LOCATION_KEY, location)
    _locationListState.value = mapLocationsList()
    loadCurrentWeather()
  }

  private fun <T> saveToSharedPreferences(key: String, value: T) {
    val jsonString = gson.toJson(value)
    sharedPreferences.edit().putString(key, jsonString).apply()
  }

  private fun addLocationToSharedPreferences(location: Location) {
    val oldLocationsJson = sharedPreferences.getString(LOCATIONS_KEY, null)
    val updatedLocationsList: MutableList<Location> = oldLocationsJson?.let {
      val locationsList: MutableList<Location> = gson.fromJson(it)
      if (!locationsList.contains(location)) locationsList.add(location)
      locationsList
    } ?: mutableListOf(location)

    saveToSharedPreferences(LOCATIONS_KEY, updatedLocationsList)

    loadCurrentWeather()

    // TODO - remove (testing chunk)
    val savedLocations = sharedPreferences.getString(LOCATIONS_KEY, "savedLocations default")
    log("add location to shared preferences: Old locations list json = $oldLocationsJson")
    log("add location to shared preferences: New locations list json = $savedLocations")
    log("add location to shared preferences: New locations list = $updatedLocationsList")
  }

  private fun mapLocationsList(): List<Location> {
    val currentLocation = getCurrentLocation()

    val rawLocationsList = sharedPreferences.getString(LOCATIONS_KEY, null)
    val locationsList = rawLocationsList?.let { gson.fromJson<MutableList<Location>>(it) }

    val mappedLocationList = mutableListOf<Location>()
    locationsList?.forEach {
      log("mapLocationsList each item: $it")
      mappedLocationList.add(it.copy(isSelected = it.coordinates == currentLocation?.coordinates))
    }
    return mappedLocationList
  }

  private fun getCurrentLocation(): Location? {
    val rawCurrentLocation = sharedPreferences.getString(CURRENT_LOCATION_KEY, null)
    log("getCurrentLocation(): ${gson.fromJson(rawCurrentLocation, Location::class.java)}")
    return gson.fromJson(rawCurrentLocation, Location::class.java)
  }
}
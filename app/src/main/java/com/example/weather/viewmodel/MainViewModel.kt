package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.convertToModel
import com.example.weather.data.model.Coordinates
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.location.Location
import com.example.weather.repository.LocationRepository
import com.example.weather.repository.WeatherRepository
import com.example.weather.state.RequestState
import com.example.weather.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val weatherRepository: WeatherRepository,
  private val locationRepository: LocationRepository
  ): ViewModel() {

  private val _currentWeatherLiveData: MutableLiveData<CurrentWeather> = MutableLiveData()
  val currentWeatherLiveData: LiveData<CurrentWeather> = _currentWeatherLiveData

  private val _locationLiveData: MutableLiveData<Location> = MutableLiveData()
  val locationLiveData: LiveData<Location> = _locationLiveData

  fun loadCurrentWeather(coordinates: Coordinates) {
    viewModelScope.launch {
      log("GET current weather STARTED")
      when (val response = weatherRepository.loadCurrentWeather(coordinates)) {
        is RequestState.Success -> {
          val currentWeather = response.response.convertToModel()
          _currentWeatherLiveData.postValue(currentWeather)
          log("GET current weather RESULT: $currentWeather")
          log("GET current weather RESULT: ${response.response}")
        }
        is RequestState.Error -> { log("GET current weather ERROR: ${response.exception.message}") }
      }
      log("GET current weather DONE")
    }
  }

  fun loadCoordinates(query: String) {
    log("GET coordinates STARTED")
    viewModelScope.launch {
      when (val response = locationRepository.loadCoordinates(query)) {
        is RequestState.Success -> {
          val location = response.response.convertToModel()
          _locationLiveData.postValue(location)
          log("GET coordinates RESULT: $location")
          log("GET coordinates RESULT: ${response.response}")
        }
        is RequestState.Error -> { log("GET coordinates ERROR: ${response.exception.message}") }
      }
      log("GET coordinates DONE")
    }
  }
}
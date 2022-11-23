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
import com.example.weather.state.UiState
import com.example.weather.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val weatherRepository: WeatherRepository,
  private val locationRepository: LocationRepository
  ): ViewModel() {

  private val _currentWeatherLiveData: MutableLiveData<UiState<CurrentWeather>> = MutableLiveData()
  val currentWeatherLiveData: LiveData<UiState<CurrentWeather>> = _currentWeatherLiveData

  private val _locationLiveData: MutableLiveData<UiState<Location>> = MutableLiveData()
  val locationLiveData: LiveData<UiState<Location>> = _locationLiveData

  fun loadCurrentWeather(coordinates: Coordinates) {
    viewModelScope.launch {
      _currentWeatherLiveData.postValue(UiState.Loading())
      log("GET current weather STARTED")
      when (val response = weatherRepository.loadCurrentWeather(coordinates)) {
        is RequestState.Success -> {
          val currentWeather = response.response.convertToModel()
          _currentWeatherLiveData.postValue(UiState.Success(currentWeather))
          log("GET current weather RESULT  model: $currentWeather")
          log("GET current weather RESULT entity: ${response.response}")
        }
        is RequestState.Error -> {
          _currentWeatherLiveData.postValue(UiState.Error())
          log("GET current weather ERROR: ${response.exception.message}")
        }
      }
      log("GET current weather DONE")
    }
  }

  fun loadCoordinates(query: String) {
    viewModelScope.launch {
      _locationLiveData.postValue(UiState.Loading())
      log("GET coordinates STARTED")
      when (val response = locationRepository.loadCoordinates(query)) {
        is RequestState.Success -> {
          val location = response.response.convertToModel()
          _locationLiveData.postValue(UiState.Success(location))
          log("GET coordinates RESULT  model: $location")
          log("GET coordinates RESULT entity: ${response.response}")
        }
        is RequestState.Error -> {
          _locationLiveData.postValue(UiState.Error())
          log("GET coordinates ERROR: ${response.exception.message}")
        }
      }
      log("GET coordinates DONE")
    }
  }
}
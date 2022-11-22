package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.convertToModel
import com.example.weather.repository.WeatherRepository
import com.example.weather.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

  private val _currentWeatherLiveData: MutableLiveData<CurrentWeather> = MutableLiveData()
  val currentWeatherLiveData: LiveData<CurrentWeather> = _currentWeatherLiveData

  private val _precipitationsListLiveData: MutableLiveData<List<Any>> = MutableLiveData()
  val precipitationsListLiveData: LiveData<List<Any>> = _precipitationsListLiveData

  fun loadCurrentWeather() {
    viewModelScope.launch {
      when (val response = repository.loadCurrentWeather()) {
        is RequestState.Success -> {
          val currentWeather = response.response.convertToModel()
          _currentWeatherLiveData.postValue(currentWeather)

        }
        is RequestState.Error -> {}
      }
    }
  }
}
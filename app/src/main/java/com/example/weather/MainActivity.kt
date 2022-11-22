package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import com.example.weather.data.model.CurrentWeather
import com.example.weather.ui.LocationScreen
import com.example.weather.ui.WeatherScreen
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WeatherTheme {
        LocationScreen(locationCallback = {
          viewModel.loadCoordinates(it)
        })
        val locationState = viewModel.locationLiveData.observeAsState()
        locationState.value?.let {
          viewModel.loadCurrentWeather(it.coordinates)
        }
        val currentWeatherState = viewModel.currentWeatherLiveData.observeAsState()
        currentWeatherState.value?.let {
          WeatherScreen(currentWeatherState = currentWeatherState as State<CurrentWeather>)
        }
      }
    }
  }
}
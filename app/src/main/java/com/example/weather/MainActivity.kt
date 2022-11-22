package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.data.model.CurrentWeather
import com.example.weather.ui.WeatherScreen
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<WeatherViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.loadCurrentWeather()
    setContent {
      WeatherTheme {
        val currentWeatherState = viewModel.currentWeatherLiveData.observeAsState()
        currentWeatherState.value?.let {
          WeatherScreen(currentWeatherState = currentWeatherState as State<CurrentWeather>)
        }
      }
    }
  }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  WeatherTheme {
//        Greeting("Android")
  }
}
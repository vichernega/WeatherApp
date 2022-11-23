package com.example.weather

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.state.UiState
import com.example.weather.ui.LocationScreen
import com.example.weather.ui.WeatherScreen
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.utils.log
import com.example.weather.utils.showErrorToast
import com.example.weather.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
    setContent {
      WeatherTheme {
        LocationScreen(locationCallback = {
          viewModel.loadCoordinates(it)
        })
        val locationState = viewModel.locationLiveData.observeAsState()
        locationState.value?.let {
          when (it) {
            is UiState.Success -> {
              // to prevent request in a loop use coroutine
              LaunchedEffect(true) {
                viewModel.loadCurrentWeather(it.item.coordinates)
              }
              log("LOCATION UiState SUCCESS")
            }
            is UiState.Error -> {
              showErrorToast(LocalContext.current)
              log("LOCATION UiState ERROR")
            }
            is UiState.Loading -> {
              LoadingLayout()
              log("LOCATION UiState LOADING")
            }
          }
        }

        // to prevent request in a loop ('by' instead of '=')
        val currentWeatherState by viewModel.currentWeatherLiveData.observeAsState()
        currentWeatherState?.let {
          when (it) {
            is UiState.Success -> {
              WeatherScreen(currentWeather = it.item)
              log("CURRENT WEATHER UiState SUCCESS")
            }
            is UiState.Error -> {
              showErrorToast(LocalContext.current)
              log("CURRENT WEATHER UiState ERROR")
            }
            is UiState.Loading -> {
              LoadingLayout()
              log("CURRENT WEATHER UiState LOADING")
            }
          }
        }
      }
    }
  }
}

@Composable
fun LoadingLayout() {
  Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
    Box(modifier = Modifier.wrapContentSize(Alignment.Center)) {
      CircularProgressIndicator(color = Color.White)
    }
  }
}

@Composable
fun ErrorLayout() {
  Surface(
    modifier = Modifier
      .wrapContentSize()
      .padding(20.dp),
    color = Color.Black
  ) {
    Column(
      modifier = Modifier.wrapContentSize(Alignment.Center),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_error),
        contentDescription = null,
        modifier = Modifier
          .size(100.dp)
          .align(Alignment.CenterHorizontally),
        tint = Color.White
      )
      Text(text = "Something went wrong", color = Color.White, fontSize = 14.sp)
    }
  }
}
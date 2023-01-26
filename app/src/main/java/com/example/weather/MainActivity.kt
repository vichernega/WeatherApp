package com.example.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weather.data.convertToModel
import com.example.weather.data.entity.CurrentWeatherDto
import com.example.weather.navigation.BottomNavItem
import com.example.weather.state.UiState
import com.example.weather.ui.LocationScreen
import com.example.weather.ui.WeatherScreen
import com.example.weather.ui.exampleJsonWeather
import com.example.weather.ui.theme.*
import com.example.weather.utils.log
import com.example.weather.utils.showErrorToast
import com.example.weather.utils.showToast
import com.example.weather.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    setContent {
      WeatherTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          MainScreen(viewModel)
          val loadingState by viewModel.loadingState.collectAsState()
          when (loadingState) {
            is UiState.Success -> {
              showToast(LocalContext.current, "Location request is successful")
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
            else -> {}
          }
        }
        /*// to prevent request in a loop ('by' instead of '=')
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
        }*/
      }
    }
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
  val navController = rememberNavController()
  Scaffold(
    bottomBar = { BottomNavigationBar(navController = navController) }
  ) {
    NavigationGraph(navController = navController, viewModel)
  }
}

@Composable
fun LoadingLayout() {
  Box(modifier = Modifier.wrapContentSize(Alignment.Center).background(Color.Transparent)) {
    CircularProgressIndicator(color = Color.White)
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

@Composable
fun BottomNavigationBar(navController: NavHostController) {
  val navigationItems = listOf(
    BottomNavItem.WeatherScreenItem,
    BottomNavItem.LocationsScreenItem
  )
  BottomNavigation(
    backgroundColor = BlackTransparent35,
    contentColor = Color.Black,
    modifier = Modifier.height(50.dp),
    elevation = 0.dp
  ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    navigationItems.forEach { item ->
      BottomNavigationItem(
        icon = {
          Icon(
            painterResource(id = item.icon),
            contentDescription = item.title,
            tint = Color.White,
            modifier = Modifier.size(20.dp, 20.dp)
          )
        },
        label = {
          Text(
            text = item.title,
            fontSize = 9.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
          )
        },
        selected = currentRoute == item.route,
        alwaysShowLabel = false,
        onClick = {
          navController.navigate(item.route) {
            navController.graph.startDestinationRoute?.let { route ->
              popUpTo(route) {
                saveState = true
              }
            }
            launchSingleTop = true
            restoreState = true
          }
        }
      )
    }
  }

}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MainViewModel) {
  val currentWeatherDto = Gson().fromJson(exampleJsonWeather, CurrentWeatherDto::class.java)
  val currentWeather = currentWeatherDto.convertToModel()

  NavHost(navController = navController, startDestination = BottomNavItem.WeatherScreenItem.route) {
    composable(BottomNavItem.WeatherScreenItem.route) {
      WeatherScreen(currentWeather = currentWeather)
    }
    composable(BottomNavItem.LocationsScreenItem.route) {
      LocationScreen(viewModel = viewModel)
    }
  }
}

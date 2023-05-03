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
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.location.Location
import com.example.weather.navigation.BottomNavItem
import com.example.weather.state.UiState
import com.example.weather.ui.LocationScreen
import com.example.weather.ui.MainWeatherScreen
import com.example.weather.ui.theme.*
import com.example.weather.utils.log
import com.example.weather.utils.showErrorToast
import com.example.weather.utils.showToast
import com.example.weather.viewmodel.MainViewModel
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
          val locationsList by viewModel.locationListState.collectAsState()
          val currentWeather by viewModel.currentWeatherState.collectAsState()
          MainScreen(
            currentWeather = currentWeather,
            locationsList = locationsList,
            onSearchClick = { query -> viewModel.loadCoordinates(query) },
            onLocationClick = { location -> viewModel.selectCurrentLocation(location) }
          )
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
      }
    }
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  currentWeather: CurrentWeather?,
  locationsList: List<Location>,
  onSearchClick: (query: String) -> Unit,
  onLocationClick: (location: Location) -> Unit
) {
  val navController = rememberNavController()
  Scaffold(
    bottomBar = { BottomNavigationBar(navController = navController) }
  ) {
    NavigationGraph(navController, currentWeather, locationsList, onSearchClick, onLocationClick)
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
fun NavigationGraph(
  navController: NavHostController,
  currentWeather: CurrentWeather?,
  locationsList: List<Location>,
  onSearchClick: (query: String) -> Unit,
  onLocationClick: (location: Location) -> Unit
) {
  NavHost(navController = navController, startDestination = BottomNavItem.WeatherScreenItem.route) {
    composable(BottomNavItem.WeatherScreenItem.route) {
      MainWeatherScreen(currentWeather)
    }
    composable(BottomNavItem.LocationsScreenItem.route) {
      LocationScreen(locationsList, onSearchClick, onLocationClick)
    }
  }
}

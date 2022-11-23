package com.example.weather.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.data.convertToModel
import com.example.weather.data.entity.CurrentWeatherDto
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.Precipitations
import com.example.weather.data.model.WeatherParameters
import com.example.weather.data.model.Wind
import com.example.weather.ui.theme.BlackTransparent
import com.example.weather.ui.theme.Grey
import com.example.weather.ui.theme.WhiteTransparent
import com.example.weather.utils.getRotationAngle
import com.example.weather.utils.setFontWeightToSubText
import com.google.gson.Gson

@Composable
fun WeatherScreen(currentWeather: CurrentWeather) {
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .background(brush = Brush.verticalGradient(colors = listOf(Color.LightGray, Grey)))
        .padding(horizontal = 20.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.height(30.dp))
      CurrentWeatherLayout(currentWeather = currentWeather)
      Spacer(modifier = Modifier.height(30.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        currentWeather.apply {
          wind?.let {
            WindLayout(wind = it)
          }
          precipitations?.let {
            PrecipitationsLayout(precipitations = it)
          }
          weatherParameters?.let { weatherParameters ->
            HumidityAndPressureLayout(weatherParameters = weatherParameters)
            visibility?.let { visibility ->
              VisibilityAndFeelsLikeLayout(
                weatherParameters = weatherParameters,
                visibility = visibility
              )
            }
          }
          town?.let {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
              text = setFontWeightToSubText(
                contentToChange = it,
                otherContent = "Weather for ",
                fontWeight = FontWeight.Bold,
                isAtTheEnd = false
              ),
              color = Color.White,
              fontSize = 12.sp,
              modifier = Modifier.align(Alignment.CenterHorizontally)
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
    }

    // Floating Button
  }
}

@Composable
fun CurrentWeatherLayout(currentWeather: CurrentWeather) {
  val currentTemp = currentWeather.weatherParameters?.temperature
  Box(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Location
      Text(
        text = currentWeather.town ?: "Location",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        color = Color.White,
        fontSize = 20.sp,
      )
      // Current Temperature
      if (currentTemp?.isNotEmpty() == true) {
        Text(
          text = currentTemp,
          modifier = Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally),
          color = Color.White,
          fontSize = 50.sp,
          fontWeight = FontWeight.Light
        )
      }
      // Description
      currentWeather.weather?.let {
        Text(
          text = it.description,
          modifier = Modifier.align(Alignment.CenterHorizontally),
          color = Color.White,
          fontSize = 14.sp,
        )
      }
      // Max and min Temperature
      currentWeather.weatherParameters?.let {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
          if (it.isMaxValid) {
            Text(
              text = "Highest: ${it.maxTemperature}",
              color = Color.White,
              fontSize = 14.sp
            )
          }
          if (it.isMinValid) {
            Text(
              text = "Lowest: ${it.minTemperature}",
              modifier = Modifier.padding(start = 6.dp),
              color = Color.White,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun WindLayout(wind: Wind) {
  val initialRotationDegree by remember { mutableStateOf(0f) }
  val rotation = remember { Animatable(initialRotationDegree) }
  val rotationAngle = getRotationAngle(wind.direction.toFloat())

  // animation for compass arrow
  LaunchedEffect(true) {
    rotation.animateTo(
      targetValue = rotationAngle,
      animationSpec = tween((rotationAngle * 5).toInt(), delayMillis = 500)
    )
  }

  val windSpeedFormatted = setFontWeightToSubText(
    contentToChange = wind.speed.toString(),
    otherContent = "\nmph",
    fontWeight = FontWeight.ExtraBold
  )
  val windGustFormatted = setFontWeightToSubText(
    contentToChange = wind.gust.toString(),
    otherContent = " mph"
  )

  // Layout
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(140.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {

    BoxLayout(
      modifier = Modifier.weight(1f),
      iconResource = R.drawable.ic_wind,
      titleText = "Wind"
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Icon(
          painter = painterResource(id = R.drawable.ic_compass_background),
          contentDescription = "Compass background",
          modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
          tint = WhiteTransparent
        )
        Icon(
          painter = painterResource(id = R.drawable.ic_compass_arrow),
          contentDescription = "Compass arrow",
          modifier = Modifier
            .size(100.dp)
            .align(Alignment.Center)
            .rotate(rotation.value),
          tint = Color.White
        )
        Surface(
          shadowElevation = 3.dp,
          shape = CircleShape,
          modifier = Modifier.align(Alignment.Center)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Grey)
              .align(Alignment.Center)
          ) {
            Text(
              text = windSpeedFormatted,
              color = Color.White,
              fontSize = 10.sp,
              textAlign = TextAlign.Center,
              lineHeight = 11.sp,
              modifier = Modifier.align(Alignment.Center)
            )
          }
        }
      }
    }

    BoxLayout(
      modifier = Modifier.weight(1f),
      iconResource = R.drawable.ic_wind,
      titleText = "Wind Gust"
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(text = windGustFormatted, color = Color.White, fontSize = 16.sp)
        Text(
          text = "A brief increase in the speed of the wind, usually less than 20 seconds.",
          color = Color.White,
          fontSize = 10.sp,
          lineHeight = 10.sp,
          modifier = Modifier.align(alignment = Alignment.BottomStart)
        )
      }
    }
  }
}

@Composable
fun HumidityAndPressureLayout(weatherParameters: WeatherParameters) {
  val humidityFormatted = setFontWeightToSubText(
    contentToChange = weatherParameters.humidity.toString(),
    otherContent = "%"
  )
  val pressureFormatted = setFontWeightToSubText(
    contentToChange = weatherParameters.pressure.toString(),
    otherContent = " hPa"
  )
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(140.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    BoxLayout(
      modifier = Modifier.weight(1f),
      iconResource = R.drawable.ic_humidity2,
      titleText = "Humidity"
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(text = humidityFormatted, color = Color.White, fontSize = 16.sp)
        Text(
          text = "The concentration of water vapor present in the air.",
          color = Color.White,
          fontSize = 10.sp,
          lineHeight = 10.sp,
          modifier = Modifier.align(alignment = Alignment.BottomStart)
        )
      }
    }

    BoxLayout(
      modifier = Modifier.weight(1f),
      iconResource = R.drawable.ic_pressure,
      titleText = "Pressure"
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(text = pressureFormatted, color = Color.White, fontSize = 16.sp)
        Text(
          text = "The pressure within the atmosphere of Earth.",
          color = Color.White,
          fontSize = 10.sp,
          lineHeight = 10.sp,
          modifier = Modifier.align(alignment = Alignment.BottomStart)
        )
      }
    }
  }
}

@Composable
fun VisibilityAndFeelsLikeLayout(weatherParameters: WeatherParameters, visibility: Int) {
  val visibilityFormatted = setFontWeightToSubText(
    contentToChange = visibility.toString(),
    otherContent = " m"
  )
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(140.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    BoxLayout(
      modifier = Modifier.weight(1f),
      iconResource = R.drawable.ic_eye,
      titleText = "Visibility"
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(text = visibilityFormatted, color = Color.White, fontSize = 16.sp)
        Text(
          text = "The quality or state of being visible.",
          color = Color.White,
          fontSize = 10.sp,
          lineHeight = 10.sp,
          modifier = Modifier.align(alignment = Alignment.BottomStart)
        )
      }
    }

    BoxLayout(
      modifier = Modifier.weight(1f),
      iconResource = R.drawable.ic_termometer,
      titleText = "Feels Like"
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(
          text = weatherParameters.feelsLikeTemperature,
          color = Color.White,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "Currently the temperature feels like ${weatherParameters.feelsLikeTemperature}C outside.",
          color = Color.White,
          fontSize = 10.sp,
          lineHeight = 10.sp,
          modifier = Modifier.align(alignment = Alignment.BottomStart)
        )
      }
    }

  }
}

@Composable
fun PrecipitationsLayout(precipitations: Precipitations) {
  precipitations.lastHour?.let {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      BoxLayout(
        modifier = Modifier.weight(1f),
        iconResource = R.drawable.ic_eye,
        titleText = "Last Hour"
      ) {
        PrecipitationItem(precipitation = precipitations.lastHour)
      }

      precipitations.lastThreeHours?.let {
        BoxLayout(
          modifier = Modifier.weight(1f),
          iconResource = R.drawable.ic_eye,
          titleText = "Last 3 Hours"
        ) {
          PrecipitationItem(precipitation = precipitations.lastThreeHours)
        }
      }
    }
  }
}


@Composable
fun BoxLayout(
  modifier: Modifier,
  iconResource: Int,
  titleText: String,
  MainContent: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .clip(RoundedCornerShape(10.dp))
      .background(BlackTransparent)
      .padding(10.dp)
  ) {
    Column {
      Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        Icon(
          painter = painterResource(id = iconResource),
          contentDescription = titleText,
          modifier = Modifier.size(14.dp),
          tint = WhiteTransparent
        )
        Text(
          text = titleText,
          color = WhiteTransparent,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      MainContent()
    }
  }
}

@Composable
fun PrecipitationItem(precipitation: Double) {
  val precipitationFormatted = setFontWeightToSubText(
    contentToChange = precipitation.toString(),
    otherContent = " mm"
  )

  // animation
  val precipitationOffsetY = remember { Animatable(0f) }
  LaunchedEffect(true) {
    precipitationOffsetY.animateTo(
      targetValue = -(precipitation.toFloat() * 10),
      animationSpec = tween(
        durationMillis = (precipitation * 1500).toInt(),
        delayMillis = 500
      )
    )
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Row(
      Modifier
        .wrapContentSize()
        .align(Alignment.Center),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      // value + arrow
      Row(
        modifier = Modifier
          .align(Alignment.Bottom)
          .graphicsLayer(translationY = precipitationOffsetY.value),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(text = precipitationFormatted, color = Color.White, fontSize = 10.sp)
        Icon(
          painter = painterResource(id = R.drawable.ic_precipitation_arrow),
          contentDescription = "Precipitation arrow",
          modifier = Modifier.size(width = 40.dp, height = 10.dp),
          tint = Color.White
        )
      }
      // scale
      Icon(
        painter = painterResource(id = R.drawable.ic_precipitation_background),
        contentDescription = "Precipitation background",
        modifier = Modifier
          .fillMaxHeight()
          .width(15.dp)
          .align(Alignment.CenterVertically),
        tint = WhiteTransparent
      )
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .wrapContentWidth()
          .padding(start = 2.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "10", fontSize = 12.sp, color = Color.White)
        Text(text = "0", fontSize = 12.sp, color = Color.White)
      }
    }
  }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherScreenPreview() {
  val currentWeatherDto = Gson().fromJson(exampleJsonWeather, CurrentWeatherDto::class.java)
//  val currentWeatherState =
//    MutableLiveData(currentWeatherDto.convertToModel()).observeAsState() as State<CurrentWeather>
  val currentWeather = currentWeatherDto.convertToModel()
  WeatherScreen(currentWeather = currentWeather)
}

const val exampleJsonWeather =
  "{\"coord\":{\"lon\":28.468,\"lat\":49.232},\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13d\"}],\"base\":\"stations\",\"main\":{\"temp\":0.35,\"feels_like\":-2.81,\"temp_min\":0.35,\"temp_max\":0.35,\"pressure\":1010,\"humidity\":100,\"sea_level\":1010,\"grnd_level\":977},\"visibility\":138,\"wind\":{\"speed\":2.7,\"deg\":48,\"gust\":6.39},\"snow\":{\"1h\":0.42},\"clouds\":{\"all\":100},\"dt\":1669018748,\"sys\":{\"country\":\"UA\",\"sunrise\":1669008323,\"sunset\":1669040330},\"timezone\":7200,\"id\":689558,\"name\":\"Vinnytsia\",\"cod\":200}"

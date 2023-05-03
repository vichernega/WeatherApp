package com.example.weather.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.data.model.Coordinates
import com.example.weather.data.model.location.Location
import com.example.weather.ui.theme.AlmostBlack
import com.example.weather.ui.theme.DeepGray
import com.example.weather.utils.randomColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LocationScreen(
  locationsList: List<Location>,
  onSearchClick: (query: String) -> Unit,
  onLocationClick: (location: Location) -> Unit
) {
  Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
    Column(
      modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Spacer(modifier = Modifier.height(30.dp))

      // animation for globe icon
      val rotation = remember { Animatable(0f) }
      LaunchedEffect(true) {
        rotation.animateTo(
          targetValue = 360f,
          animationSpec = tween(1200, delayMillis = 500)
        )
      }
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(
          painter = painterResource(id = R.drawable.ic_globe),
          contentDescription = "Globe icon",
          modifier = Modifier
            .size(46.dp)
            .rotate(rotation.value),
          tint = Color.White,
        )
        Text(
          text = "Locations",
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.align(Alignment.CenterVertically)
        )
      }

      // search
      var query by remember { mutableStateOf("") }
      val keyboardController = LocalSoftwareKeyboardController.current

      TextField(
        value = query,
        onValueChange = { query = it },
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .shadow(2.dp, shape = RoundedCornerShape(10.dp)),
        trailingIcon = {
          IconButton(onClick = {
            onSearchClick(query)
            keyboardController?.hide()
          }) {
            Icon(
              painter = painterResource(id = R.drawable.ic_search),
              contentDescription = "",
              tint = Color.White
            )
          }
        },
        placeholder = { Text(text = "Kyiv", color = DeepGray) },
        colors = TextFieldDefaults.textFieldColors(
          containerColor = AlmostBlack,
          textColor = Color.White,
          cursorColor = Color.White,
          disabledIndicatorColor = Color.Transparent,
          errorIndicatorColor = Color.Transparent,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
          capitalization = KeyboardCapitalization.Sentences
        )
      )

      // locations list
      if (locationsList.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.Center
        ) {
          Text(text = "No locations saved", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(locationsList) { location ->
            val locationItem: @Composable () -> Unit = remember(location) {
              @Composable {
                LocationViewItem(location = location, onClick = { onLocationClick(location) })
              }
            }
            locationItem()
          }
          item {
            Spacer(modifier = Modifier.height(44.dp))
          }
        }
      }
    }
  }
}

@Composable
fun LocationViewItem(location: Location, onClick: () -> Unit) {
  val modifier = if (location.isSelected) {
    Modifier.border(1.5.dp, color = Color.White, shape = RoundedCornerShape(20.dp))
  } else Modifier

  Box(modifier = modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .clip(shape = RoundedCornerShape(20.dp))
    .clickable { onClick() }
    .background(brush = Brush.linearGradient(listOf(randomColor(), randomColor(), randomColor())))
    .padding(20.dp, 15.dp)
  ) {
    Column {
      Text(
        text = location.city,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
      )
      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationScreenPreview() {
  LocationScreen(locationCallback = {})
}
*/

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationItemPreview() {
  val location = Location(
    city = "Vinnytsia",
    coordinates = Coordinates(longitude = 28.467975, latitude = 49.2320162),
    country = "UA",
    state = "Vinnytsia Oblast"
  )
  Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(15.dp)) {
      LocationViewItem(location) {}
      LocationViewItem(location.copy(isSelected = true)) {}
      LocationViewItem(location) {}
      LocationViewItem(location) {}
    }
  }
}

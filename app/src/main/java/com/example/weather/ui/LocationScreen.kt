package com.example.weather.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.ui.theme.AlmostBlack
import com.example.weather.ui.theme.DeepGray

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LocationScreen(locationCallback: (String) -> Unit) {
  Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
    Column(
      modifier = Modifier.wrapContentSize(align = Alignment.Center),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

      // animation for globe icon
      val rotation = remember { Animatable(0f) }
      LaunchedEffect(true) {
        rotation.animateTo(
          targetValue = 360f,
          animationSpec = tween(1200, delayMillis = 500)
        )
      }
      Icon(
        painter = painterResource(id = R.drawable.ic_globe),
        contentDescription = "Globe icon",
        modifier = Modifier
          .size(100.dp)
          .align(Alignment.CenterHorizontally)
          .rotate(rotation.value),
        tint = Color.White,
      )
      Text(
        text = "Choose location",
        color = Color.White,
        fontSize = 20.sp,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      var query by remember { mutableStateOf("") }
      val keyboardController = LocalSoftwareKeyboardController.current
      TextField(
        value = query,
        onValueChange = { query = it },
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .shadow(2.dp, shape = RoundedCornerShape(10.dp)),
        trailingIcon = {
          IconButton(onClick = {
            locationCallback(query)
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
    }
  }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationScreenPreview() {
  LocationScreen(locationCallback = {})
}
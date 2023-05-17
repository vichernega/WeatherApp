package com.example.weather.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.weather.R
import com.example.weather.data.model.WeatherMap
import com.example.weather.utils.log

@Composable
fun MapsScreen(weatherMapsList: List<WeatherMap>) {
  Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
    Column(modifier = Modifier.padding(vertical = 40.dp)) {
      Row(modifier = Modifier.padding(horizontal = 15.dp)) {
        MapIcon()
        Text(
          text = "Maps",
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.align(Alignment.CenterVertically).padding(start = 10.dp)
        )
      }

      // Content
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(weatherMapsList) {
          MapView(it)
        }
      }
    }
  }
}

@Composable
fun MapIcon() {
  val iconWidth = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    iconWidth.animateTo(
      targetValue = 46f,
      animationSpec = FloatSpringSpec(dampingRatio = 0.25f, stiffness = 35f)
    )
  }

  Box {
    Icon(
      painter = painterResource(id = R.drawable.ic_map),
      contentDescription = "Map icon",
      modifier = Modifier
        .width(iconWidth.value.dp)
        .height(46.dp),
      tint = Color.White
    )
  }
}

@Composable
fun MapView(weatherMap: WeatherMap) {
  var expanded by remember { mutableStateOf(false) }
  var fullScreenMap by remember { mutableStateOf(false) }
  val rotationAngle by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = !expanded }
        .padding(vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          painter = painterResource(id = weatherMap.iconResource),
          contentDescription = "title_icon",
          tint = Color.White,
          modifier = Modifier.size(28.dp)
        )
        Text(
          text = weatherMap.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
      }

      Icon(
        painter = painterResource(id = R.drawable.ic_arrow_expand),
        contentDescription = "arrow_expand",
        modifier = Modifier.rotate(rotationAngle),
        tint = Color.White
      )
    }

    // divider
    if (!expanded) {
      Spacer(modifier = Modifier.height(2.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth(0.9f)
          .height(1.dp)
          .background(Color.Gray)
          .align(Alignment.End)
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    AnimatedVisibility(
      visible = expanded,
      enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
      exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(320.dp),
        contentAlignment = Alignment.Center
      ) {
        WebViewMap(
          url = weatherMap.link,
          fullScreenButton = painterResource(id = R.drawable.ic_fullscreen)
        ) {
          fullScreenMap = true
        }
      }

      if (fullScreenMap) {
        Dialog(
          onDismissRequest = { fullScreenMap = false },
          properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight(0.95f),
            contentAlignment = Alignment.Center
          ) {
            WebViewMap(
              url = weatherMap.link,
              fullScreenButton = painterResource(id = R.drawable.ic_fullscreen_exit)
            ) {
              fullScreenMap = false
            }
          }
        }
      }
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewMap(url: String, fullScreenButton: Painter, onFullScreenClick: () -> Unit) {
  var isLoading by remember { mutableStateOf(true) }

  Box {
    AndroidView(
      modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(20.dp)),
      factory = {
        WebView(it).apply {
          settings.javaScriptEnabled = true
          webViewClient = object : WebViewClient() {
            override fun onReceivedError(
              view: WebView?,
              request: WebResourceRequest?,
              error: WebResourceError?
            ) {
              super.onReceivedError(view, request, error)
              log("WebView error:   $error")
              log("WebView request: $request")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
              super.onPageStarted(view, url, favicon)
              isLoading = true
              log("WebView started, url: $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
              super.onPageFinished(view, url)
              // Hide the header  and map layers selector using JavaScript injection + remove marginTop from globalMap
              val javascriptCode = "(function() {" +
                  "var navElement = document.getElementById('nav-website');" +
                  "if (navElement) {" +
                  "    navElement.style.display = 'none';" +
                  "}" +
                  "var weatherElement = document.querySelector('.weather-control-layers-new');" +
                  "if (weatherElement) {" +
                  "    weatherElement.style.display = 'none';" +
                  "    weatherElement.style.visibility = 'hidden';" +
                  "}" +
                  "var globalMapElement = document.querySelector('.global-map');" +
                  "if (globalMapElement) {" +
                  "    globalMapElement.style.top = '0px';" +
                  "}" +
                  "})();"
              view?.evaluateJavascript(javascriptCode, null)
              isLoading = false
            }
          }
          loadUrl(url)
        }
      },
      update = {
        it.loadUrl(url)
      })

    if (isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(20.dp))
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(color = Color.Black, modifier = Modifier.align(Alignment.Center))
      }
    } else {
      IconButton(
        onClick = { onFullScreenClick() },
        modifier = Modifier.align(Alignment.BottomEnd)
      ) {
        Icon(
          painter = fullScreenButton,
          contentDescription = "full screen"
        )
      }
    }
  }
}
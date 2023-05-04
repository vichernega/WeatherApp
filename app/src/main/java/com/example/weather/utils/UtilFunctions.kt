package com.example.weather.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.core.graphics.ColorUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun setFontWeightToSubText(
  contentToChange: String,
  otherContent: String,
  fontWeight: FontWeight = FontWeight.SemiBold,
  isAtTheEnd: Boolean = true
): AnnotatedString {
  return buildAnnotatedString {
    if (isAtTheEnd) {
      withStyle(style = SpanStyle(fontWeight = fontWeight)) {
        append(contentToChange)
      }
      append(otherContent)
    } else {
      append(otherContent)
      withStyle(style = SpanStyle(fontWeight = fontWeight)) {
        append(contentToChange)
      }
    }
  }
}

fun getRotationAngle(angle: Float) = if (angle > 90f) angle - 90f else angle

fun randomColor(alpha: Float = 0.8f) = Color.hsl(
  hue = randomFloat(130..240),
  saturation = randomFloat(300..500) / 1000f,
  lightness = randomFloat(650..750) / 1000f,
  alpha = alpha
)

private fun randomFloat(range: IntRange) = range.random().toFloat()

fun Color.adjustDarkness(darkness: Float): Color {
  val hsv = FloatArray(3)
  ColorUtils.colorToHSL(this.toArgb(), hsv)
  hsv[2] *= darkness
  return Color(ColorUtils.HSLToColor(hsv))
}

fun showToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showErrorToast(context: Context) {
  showToast(context, "Something went wrong.. Please try again")
}

fun log(content: String) {
  Log.d("VICH", content)
}

internal inline fun <reified T> Gson.fromJson(json: String) =
  fromJson<T>(json, object : TypeToken<T>() {}.type)

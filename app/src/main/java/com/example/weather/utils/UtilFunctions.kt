package com.example.weather.utils

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

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

fun log(content: String) {
  Log.d("VICH", content)
}
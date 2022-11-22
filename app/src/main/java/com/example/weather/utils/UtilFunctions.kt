package com.example.weather.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun setFontWeightSubText(firstStr: String, secondStr: String, fontWeight: FontWeight = FontWeight.SemiBold): AnnotatedString {
  return buildAnnotatedString {
    withStyle(style = SpanStyle(fontWeight = fontWeight)) {
      append(firstStr)
    }
    append(secondStr)
  }
}
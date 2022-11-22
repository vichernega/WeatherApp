package com.example.weather.state

sealed interface UiState<T> {
  class Success<T>(val item: T): UiState<T>
  class Error<T>: UiState<T>
  class Loading<T>: UiState<T>
}
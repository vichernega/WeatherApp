package com.example.weather.state

sealed interface UiState<T> {
  class Success<T>: UiState<T>
  class Error<T>: UiState<T>
  class Loading<T>: UiState<T>
}
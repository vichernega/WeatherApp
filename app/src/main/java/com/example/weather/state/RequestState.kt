package com.example.weather.state

sealed class RequestState<T>() {
  class Success<T>(val response: T): RequestState<T>()
  class Error<T>: RequestState<T>()
}

package com.example.weather.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun <T> ViewModel.loadRequest(request: () -> RequestState<T>) {
  viewModelScope.launch {
    val response = request.invoke()
    /*when(response) {
      is RequestState.Success<T> ->
    }*/
  }
}
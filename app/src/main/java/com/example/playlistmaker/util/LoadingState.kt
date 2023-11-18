package com.example.playlistmaker.util

sealed interface LoadingState<T> {
    data class Success<T>(val data: T) : LoadingState<T>
    data class Error<T>(val message: String) : LoadingState<T>
}
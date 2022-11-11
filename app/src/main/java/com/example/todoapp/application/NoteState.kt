package com.example.todoapp.application

sealed class NoteState<T>(val data: T? = null, val error: String? = null) {
    class Loading<T> : NoteState<T>(error = null)
    class Success<T>(data: T?) : NoteState<T>(error = null, data = data)
    class Error<T>(error: String) : NoteState<T>(error = error)
}
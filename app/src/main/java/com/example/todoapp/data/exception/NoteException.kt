package com.example.todoapp.data.exception

sealed class NoteException: Exception() {
    object ServerException : NoteException()
    object ClientException : NoteException()
}
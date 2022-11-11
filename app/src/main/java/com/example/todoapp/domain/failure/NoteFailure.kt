package com.example.todoapp.domain.failure

sealed class NoteFailure {
    object ServerFailure : NoteFailure()
    object ClientFailure : NoteFailure()
}

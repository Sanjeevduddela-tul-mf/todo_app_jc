package com.example.todoapp.domain.usecase

import arrow.core.Either
import com.example.todoapp.domain.entity.Note
import com.example.todoapp.domain.failure.NoteFailure
import com.example.todoapp.domain.repository.INoteRepository
import javax.inject.Inject

class AddNote @Inject constructor(private val repository: INoteRepository) {
    suspend operator fun invoke(note: Note): Either<NoteFailure, Note> = repository.addNote(note)
}
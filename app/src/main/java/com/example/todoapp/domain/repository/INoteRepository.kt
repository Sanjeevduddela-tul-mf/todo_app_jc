package com.example.todoapp.domain.repository

import arrow.core.Either
import com.example.todoapp.domain.entity.Note
import com.example.todoapp.domain.failure.NoteFailure

interface INoteRepository {
    suspend fun addNote(note: Note): Either<NoteFailure, Note>
    suspend fun deleteNote(note: Note): Either<NoteFailure, Note>
    suspend fun editNote(note: Note): Either<NoteFailure, Note>
    suspend fun getAllNotes(): Either<NoteFailure, List<Note>>
}
package com.example.todoapp.data.datasource

import com.example.todoapp.data.model.NoteDto
import com.example.todoapp.domain.entity.Note

interface INoteRemoteDataSource {
    suspend fun addNote(note: Note)
    suspend fun editNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun getAllNote(): List<NoteDto>
}

class NoteRemoteDataSource : INoteRemoteDataSource {
    override suspend fun addNote(note: Note) {}
    override suspend fun editNote(note: Note) {}
    override suspend fun deleteNote(note: Note) {}
    override suspend fun getAllNote(): List<NoteDto> {
        val notes = mutableListOf<NoteDto>()
        notes.add(NoteDto("1", "This is a sample note 1", "This is a sample note description", false))
        notes.add(NoteDto("1", "This is a sample note 2", "This is a sample note description", false))
        notes.add(NoteDto("1", "This is a sample note 3", "This is a sample note description", false))
        notes.add(NoteDto("1", "This is a sample note 4", "This is a sample note description", false))
        return notes.toList()
    }
}
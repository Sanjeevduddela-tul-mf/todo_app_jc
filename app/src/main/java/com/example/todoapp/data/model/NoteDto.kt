package com.example.todoapp.data.model

import com.example.todoapp.domain.entity.Note

class NoteDto(
    private val id: String,
    private val title: String,
    private val des: String,
    private val isDone: Boolean,
) {
    fun toNote(): Note = Note(id, title, des, isDone)
}
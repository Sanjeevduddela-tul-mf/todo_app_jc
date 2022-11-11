package com.example.todoapp.application

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.core.di.util.InputValidator
import com.example.todoapp.core.di.util.RandomIDGenerator
import com.example.todoapp.domain.entity.Note
import com.example.todoapp.domain.failure.NoteFailure
import com.example.todoapp.domain.usecase.AddNote
import com.example.todoapp.domain.usecase.GetNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNote,
    private val getNotesUseCase: GetNotes,
    private val inputValidator: InputValidator,
    private val randomIdGenerator: RandomIDGenerator
) :
    ViewModel() {
    private val _state = mutableStateOf<NoteState<MutableList<Note>>>(NoteState.Loading())
    val state get() = _state
    private val _addState = mutableStateOf<NoteState<Note>>(NoteState.Loading())
    val addState get() = _addState

    init {
        getNotes()
    }

    private fun getNotes() {
        _state.value = NoteState.Loading()
        viewModelScope.launch {
            getNotesUseCase().fold(
                ifLeft = {
                    _state.value = NoteState.Error(
                        when (it) {
                            NoteFailure.ClientFailure -> "Client Failure"
                            NoteFailure.ServerFailure -> "Server Failure"
                        }
                    )
                },
                ifRight = {
                    _state.value = NoteState.Success(it.toMutableList())
                }
            )
        }
    }

    fun addNote(title: String, des: String) {
        _addState.value = NoteState.Loading()

        val note = Note(
            randomIdGenerator.generate(),
            inputValidator.trim(title),
            inputValidator.trim(des),
            false
        )
        viewModelScope.launch {
            addNoteUseCase(note).fold(
                ifLeft = {
                    _addState.value = NoteState.Error(
                        when (it) {
                            NoteFailure.ClientFailure -> "Client Failure"
                            NoteFailure.ServerFailure -> "Server Failure"
                        }
                    )
                },
                ifRight = {
                    _addState.value = NoteState.Success(it)
                    // Add new Note to list
                    _state.value = NoteState.Success(_state.value.data
                        ?.apply { add(0, it) })
                }
            )
        }

    }

    fun editNote(index: Int, title: String, des: String) {
        if (_state.value.data != null) {
            val notes: MutableList<Note> = _state.value.data!!
            notes[index] = _state.value.data?.get(index)?.copy(title = title, des = des)!!
            _state.value = NoteState.Success(notes)
        }
    }

    fun toggleCompleteNote(note: Note, value: Boolean) {
        val notes: MutableList<Note> = _state.value.data!!
        val index = notes.indexOf(note)
        notes[index] = _state.value.data?.get(index)?.copy(isDone = value)!!
        _state.value = NoteState.Success(notes)
    }
}
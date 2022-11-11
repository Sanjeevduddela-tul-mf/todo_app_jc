package com.example.todoapp.data.repository

import arrow.core.Either
import com.example.todoapp.data.datasource.INoteRemoteDataSource
import com.example.todoapp.data.exception.NoteException
import com.example.todoapp.domain.entity.Note
import com.example.todoapp.domain.failure.NoteFailure
import com.example.todoapp.domain.repository.INoteRepository
import javax.inject.Inject

class NoteRepository @Inject constructor(private val dataSource: INoteRemoteDataSource) : INoteRepository {
    override suspend fun addNote(note: Note): Either<NoteFailure, Note> {
        return try {
            dataSource.addNote(note)
            Either.Right(note)
        } catch (e: NoteException) {
            when (e) {
                NoteException.ClientException -> Either.Left(NoteFailure.ClientFailure)
                NoteException.ServerException -> Either.Left(NoteFailure.ServerFailure)
            }
        }
    }

    override suspend fun deleteNote(note: Note): Either<NoteFailure, Note> {
        return try {
            dataSource.addNote(note)
            Either.Right(note)
        } catch (e: NoteException) {
            when (e) {
                NoteException.ClientException -> Either.Left(NoteFailure.ClientFailure)
                NoteException.ServerException -> Either.Left(NoteFailure.ServerFailure)
            }
        }
    }

    override suspend fun editNote(note: Note): Either<NoteFailure, Note> {
        return try {
            dataSource.addNote(note)
            Either.Right(note)
        } catch (e: NoteException) {
            when (e) {
                NoteException.ClientException -> Either.Left(NoteFailure.ClientFailure)
                NoteException.ServerException -> Either.Left(NoteFailure.ServerFailure)
            }
        }
    }

    override suspend fun getAllNotes(): Either<NoteFailure, List<Note>> {
        return try {
            Either.Right(dataSource.getAllNote().map { it.toNote() })
        } catch (e: NoteException) {
            return when (e) {
                NoteException.ClientException -> Either.Left(NoteFailure.ClientFailure)
                NoteException.ServerException -> Either.Left(NoteFailure.ServerFailure)
            }
        }
    }
}
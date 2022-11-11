package com.example.todoapp.core.di

import com.example.todoapp.core.di.util.InputValidator
import com.example.todoapp.core.di.util.RandomIDGenerator
import com.example.todoapp.data.datasource.INoteRemoteDataSource
import com.example.todoapp.data.datasource.NoteRemoteDataSource
import com.example.todoapp.data.repository.NoteRepository
import com.example.todoapp.domain.repository.INoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteRemoteDataSource(): INoteRemoteDataSource = NoteRemoteDataSource()

    @Provides
    @Singleton
    fun provideNoteRepository(dataSource: INoteRemoteDataSource): INoteRepository = NoteRepository(dataSource)

    @Provides
    @Singleton
    fun providesInputValidator(): InputValidator {
        return InputValidator
    }

    @Provides
    @Singleton
    fun providesRandomIDGenerator(): RandomIDGenerator {
        return RandomIDGenerator
    }
}
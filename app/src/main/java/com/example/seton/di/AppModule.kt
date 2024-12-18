package com.example.seton.di

import android.app.Application
import androidx.room.Room
import com.example.seton.common.data.local.AppDatabase
import com.example.seton.feature_notes.data.repository.NoteRepositoryImpl
import com.example.seton.feature_notes.domain.repository.NoteRepository
import com.example.seton.feature_notes.domain.use_case.DeleteAllNotes
import com.example.seton.feature_notes.domain.use_case.DeleteNote
import com.example.seton.feature_notes.domain.use_case.DeleteNoteById
import com.example.seton.feature_notes.domain.use_case.GetAllNotes
import com.example.seton.feature_notes.domain.use_case.GetNoteById
import com.example.seton.feature_notes.domain.use_case.NoteUseCases
import com.example.seton.feature_notes.domain.use_case.SaveNote
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
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: AppDatabase): NoteRepository {
        return NoteRepositoryImpl(database.noteDao())
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotes = GetAllNotes(repository),
            deleteNote = DeleteNote(repository),
            deleteAllNotes = DeleteAllNotes(repository),
            saveNote = SaveNote(repository),
            getNoteById = GetNoteById(repository),
            deleteNoteById = DeleteNoteById(repository)
        )
    }
}
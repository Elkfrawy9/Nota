package com.elkfrawy.nota.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elkfrawy.nota.feature_note.data.repository.NoteRepositoryImpl
import com.elkfrawy.nota.feature_note.data.source.NoteDao
import com.elkfrawy.nota.feature_note.data.source.NoteDatabase
import com.elkfrawy.nota.feature_note.domain.repository.NoteRepository
import com.elkfrawy.nota.feature_note.domain.usecase.DeleteAllNoteUseCase
import com.elkfrawy.nota.feature_note.domain.usecase.DeleteNoteUseCase
import com.elkfrawy.nota.feature_note.domain.usecase.GetNoteByIdUseCase
import com.elkfrawy.nota.feature_note.domain.usecase.GetNotesUseCase
import com.elkfrawy.nota.feature_note.domain.usecase.InsertNoteUseCase
import com.elkfrawy.nota.feature_note.domain.usecase.NoteUseCases
import com.elkfrawy.nota.feature_note.domain.usecase.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Application): NoteDatabase{
        return Room.databaseBuilder(context, NoteDatabase::class.java, "Nota").build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao{
        return db.dao
    }

    @Singleton
    @Provides
    fun provideNoteRepository(dao: NoteDao): NoteRepository{
       return NoteRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideNoteUseCases(noteRepo: NoteRepository): NoteUseCases{
        return NoteUseCases(
            deleteNote = DeleteNoteUseCase(noteRepo),
            updateNote = UpdateNoteUseCase(noteRepo),
            insertNote = InsertNoteUseCase(noteRepo),
            getNotes = GetNotesUseCase(noteRepo),
            getNoteById = GetNoteByIdUseCase(noteRepo),
            deleteAllNotes = DeleteAllNoteUseCase(noteRepo),
        )
    }
}
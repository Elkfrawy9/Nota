package com.elkfrawy.nota.feature_note.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elkfrawy.nota.feature_note.domain.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase(){

    abstract val dao: NoteDao

}
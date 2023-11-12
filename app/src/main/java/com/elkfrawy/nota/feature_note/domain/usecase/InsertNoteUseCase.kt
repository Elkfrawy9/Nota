package com.elkfrawy.nota.feature_note.domain.usecase

import com.elkfrawy.nota.feature_note.domain.model.InvalidNoteException
import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.repository.NoteRepository
import javax.inject.Inject
import kotlin.jvm.Throws

class InsertNoteUseCase (private val noteRepo: NoteRepository) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isEmpty()) throw InvalidNoteException("The title is empty!")
        if (note.content.isEmpty()) throw InvalidNoteException("The content is empty!")
        noteRepo.insertNote(note)
    }

}
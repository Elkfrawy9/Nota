package com.elkfrawy.nota.feature_note.domain.usecase

import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteAllNoteUseCase(private val noteRepo: NoteRepository) {

    suspend operator fun invoke() = noteRepo.deleteAllNotes()

}
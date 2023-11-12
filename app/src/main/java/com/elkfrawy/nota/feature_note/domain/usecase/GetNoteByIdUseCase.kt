package com.elkfrawy.nota.feature_note.domain.usecase

import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase (private val noteRepo: NoteRepository) {

    suspend operator fun invoke(id: Int) = noteRepo.getNoteById(id)

}
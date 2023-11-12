package com.elkfrawy.nota.feature_note.domain.usecase

import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.repository.NoteRepository
import com.elkfrawy.nota.feature_note.domain.util.NoteOrder
import com.elkfrawy.nota.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase(private val noteRepo: NoteRepository) {

    operator fun invoke(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)): Flow<List<Note>> {
        return noteRepo.getNotes().map {
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Date -> { it.sortedBy { it.timestamp } }
                        is NoteOrder.Title -> { it.sortedBy { it.title.lowercase() } }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Date -> { it.sortedByDescending { it.timestamp } }
                        is NoteOrder.Title -> { it.sortedByDescending { it.title.lowercase() } }
                    }
                }
            }
        }
    }

}
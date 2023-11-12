package com.elkfrawy.nota.feature_note.presentation.note

import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.util.NoteOrder
import com.elkfrawy.nota.feature_note.domain.util.OrderType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val order: NoteOrder = NoteOrder.Date(OrderType.Descending),
)

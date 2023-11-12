package com.elkfrawy.nota.feature_note.presentation.note

import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.util.NoteOrder

sealed class NoteEvents{

    data class Order(val noteOrder: NoteOrder): NoteEvents()
    data class DeleteNode(val note: Note): NoteEvents()
    object DeleteAll: NoteEvents()
    object RestoreNode: NoteEvents()


}

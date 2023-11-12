package com.elkfrawy.nota.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import com.elkfrawy.nota.feature_note.domain.model.Note

sealed class AddEditNoteEvent{
    data class EnteredTitle(val title: String): AddEditNoteEvent()
    data class EnteredContent(val content: String): AddEditNoteEvent()
    data class TitleFocusState(val content: FocusState): AddEditNoteEvent()
    data class ContentFocusState(val content: FocusState): AddEditNoteEvent()
    data class ChangeColor(val color: Int): AddEditNoteEvent()
    object InsertNote: AddEditNoteEvent()
    object UpdateNote: AddEditNoteEvent()

}

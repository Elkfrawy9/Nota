package com.elkfrawy.nota.feature_note.domain.usecase

data class NoteUseCases (
    val deleteNote: DeleteNoteUseCase,
    val updateNote: UpdateNoteUseCase,
    val insertNote: InsertNoteUseCase,
    val getNotes: GetNotesUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val deleteAllNotes: DeleteAllNoteUseCase,
)
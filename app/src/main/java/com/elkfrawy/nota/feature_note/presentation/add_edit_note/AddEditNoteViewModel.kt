package com.elkfrawy.nota.feature_note.presentation.add_edit_note

import android.provider.ContactsContract
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elkfrawy.nota.feature_note.domain.model.InvalidNoteException
import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.usecase.NoteUseCases
import com.elkfrawy.nota.feature_note.presentation.note.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(private val noteUseCases: NoteUseCases,
                                               savedStateHandle: SavedStateHandle) :
    ViewModel() {

    init {
        savedStateHandle.get<Int>("noteId")?.let {
            println("Farrag: viewModel ID-> $it")
            if (it != -1){
                viewModelScope.launch {
                    noteUseCases.getNoteById.invoke(it)?.let {
                        noteId = it.id
                        _titleState.value = titleState.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )
                        _contentState.value = contentState.value.copy(
                            text = it.content,
                            isHintVisible = false
                        )
                        _colorState.value = it.color
                    }
                }
            }
        }
    }

    var noteId: Int? = null

    private val _titleState = mutableStateOf(
        NoteTextFieldState(
            hint = "Your Title..."
        )
    )
    val titleState: State<NoteTextFieldState> = _titleState

    private val _contentState = mutableStateOf(
        NoteTextFieldState(
            hint = "Your Content..."
        )
    )
    val contentState: State<NoteTextFieldState> = _contentState

    private val _colorState = mutableStateOf(Note.noteColors.random().toArgb())
    val colorState: State<Int> = _colorState

    private val _uiState = MutableSharedFlow<UiEvent>()
    val uiState: SharedFlow<UiEvent> = _uiState.asSharedFlow()

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _titleState.value = titleState.value.copy(
                    text = event.title,
                    isHintVisible = false,
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _contentState.value = contentState.value.copy(
                    text = event.content,
                    isHintVisible = false,
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _colorState.value = event.color
            }

            is AddEditNoteEvent.ContentFocusState -> {
                _contentState.value = contentState.value.copy(
                    isHintVisible = !event.content.isFocused
                )
            }

            AddEditNoteEvent.InsertNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.insertNote.invoke(
                            Note(
                                title = titleState.value.text,
                                content = contentState.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = colorState.value,
                            )
                        )
                        _uiState.emit(UiEvent.InsertNote)

                    } catch (ex: InvalidNoteException) {
                        _uiState.emit(UiEvent.ShowSnackbar(ex.message ?: "Unknown Error"))
                    }
                }
            }

            is AddEditNoteEvent.TitleFocusState -> {
                _titleState.value = titleState.value.copy(
                    isHintVisible = !event.content.isFocused
                )
            }

            AddEditNoteEvent.UpdateNote -> {
                viewModelScope.launch {
                    noteUseCases.updateNote.invoke(
                        Note(
                            id = noteId,
                            title = titleState.value.text,
                            content = contentState.value.text,
                            timestamp = System.currentTimeMillis(),
                            color = colorState.value,
                        )
                    )
                    _uiState.emit(UiEvent.InsertNote)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object InsertNote : UiEvent()
    }


}
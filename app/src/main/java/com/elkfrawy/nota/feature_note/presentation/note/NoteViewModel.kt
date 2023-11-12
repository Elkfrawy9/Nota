package com.elkfrawy.nota.feature_note.presentation.note

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.domain.usecase.NoteUseCases
import com.elkfrawy.nota.feature_note.domain.util.NoteOrder
import com.elkfrawy.nota.feature_note.domain.util.OrderType
import com.elkfrawy.nota.feature_note.presentation.note.NoteEvents
import com.elkfrawy.nota.feature_note.presentation.note.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesUseCases: NoteUseCases) : ViewModel() {

    private val _state = mutableStateOf<NoteState>(NoteState())
    val state: State<NoteState> = _state

    var deletedNote: Note? = null
    private var noteJob: Job?= null

    init {
        getNodes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NoteEvents) {
        when (event) {
            is NoteEvents.Order -> {
                if (state.value.order::class == event.noteOrder::class
                    && state.value.order.orderType::class == event.noteOrder.orderType::class) return
                getNodes(event.noteOrder)
            }

            is NoteEvents.DeleteNode -> {
                viewModelScope.launch {
                    notesUseCases.deleteNote.invoke(event.note)
                    deletedNote = event.note
                }
            }

            is NoteEvents.DeleteAll -> {
                viewModelScope.launch {
                    notesUseCases.deleteAllNotes.invoke()
                }
            }

            is NoteEvents.RestoreNode -> {

                viewModelScope.launch {
                    notesUseCases.insertNote.invoke(deletedNote ?: return@launch)
                }
                deletedNote = null
            }

        }
    }


    fun getNodes(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)) {
        noteJob?.cancel()
        noteJob = notesUseCases.getNotes.invoke(noteOrder).onEach {
            _state.value = _state.value.copy(notes = it, order = noteOrder)
        }.launchIn(viewModelScope)
    }


}
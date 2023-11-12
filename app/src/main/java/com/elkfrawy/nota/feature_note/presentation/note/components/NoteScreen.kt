@file:OptIn(ExperimentalMaterial3Api::class)

package com.elkfrawy.nota.feature_note.presentation.note.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elkfrawy.nota.feature_note.presentation.note.NoteViewModel
import com.elkfrawy.nota.feature_note.presentation.note.NoteEvents
import com.elkfrawy.nota.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    viewModel: NoteViewModel = hiltViewModel(),
    controller: NavController
) {

    val nestedMenuSelection = remember() {
        mutableStateOf(false)
    }

    val state = viewModel.state.value
    val scope = rememberCoroutineScope()

    val snackbarHost = remember {
        SnackbarHostState()
    }

    ScaffoldSection(
        snackbarHost,
        nestedMenuSelection = nestedMenuSelection,
        onMenuSelected = { order ->
            viewModel.onEvent(NoteEvents.Order(order))
            nestedMenuSelection.value = false
        },
        onDeleteAll = {
            viewModel.onEvent(NoteEvents.DeleteAll)
        },
        onFabClick = {
            controller.navigate(Screen.AddEditNoteScreen.route)
        }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(state.notes) {
                NoteItem(note = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable {
                            controller.navigate(
                                Screen.AddEditNoteScreen.route +
                                        "?noteId=${it.id}&color=${it.color}"
                            )

                        }) {
                    viewModel.onEvent(NoteEvents.DeleteNode(it))
                    scope.launch {
                        val result = snackbarHost.showSnackbar(
                            message = "Note Deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(NoteEvents.RestoreNode)
                    }
                }
            }
        }
    }

}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.elkfrawy.nota.feature_note.presentation.add_edit_note.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.elkfrawy.nota.R
import com.elkfrawy.nota.feature_note.domain.model.Note
import com.elkfrawy.nota.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.elkfrawy.nota.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    color: Int,
    controller: NavController,
    ) {


    val snackbarState = remember {
        SnackbarHostState()
    }

    val titleState = viewModel.titleState.value
    val contentState = viewModel.contentState.value
    val scope = rememberCoroutineScope()
    val animateColor = remember {
        Animatable(if (color != -1) Color(color) else Color(viewModel.colorState.value))
    }

    LaunchedEffect(key1 = true){
        viewModel.uiState.collectLatest {
            when(it){
                is AddEditNoteViewModel.UiEvent.InsertNote->{controller.navigateUp()}
                is AddEditNoteViewModel.UiEvent.ShowSnackbar->{
                    snackbarState.showSnackbar(
                        message = it.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }

    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (viewModel.noteId == null)
                    viewModel.onEvent(AddEditNoteEvent.InsertNote)
                else
                    viewModel.onEvent(AddEditNoteEvent.UpdateNote)

            }) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_save), contentDescription = "Save Note")
            }

        },
        snackbarHost = { SnackbarHost(snackbarState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(animateColor.value)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Note.noteColors.forEach {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(12.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(it.toArgb()))
                            .border(
                                4.dp,
                                color = if (viewModel.colorState.value == it.toArgb()) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(it.toArgb()))
                                    animateColor.animateTo(
                                        targetValue = Color(it.toArgb()),
                                        animationSpec = tween(
                                            500
                                        )
                                    )

                                }

                            }
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            NoteTextField(
                hint = titleState.hint,
                text = titleState.text,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.onEvent(AddEditNoteEvent.TitleFocusState(it))
                    }
            )

            NoteTextField(
                hint = contentState.hint,
                text = contentState.text,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged {
                        viewModel.onEvent(AddEditNoteEvent.ContentFocusState(it))
                    },
            )


        }

    }


}
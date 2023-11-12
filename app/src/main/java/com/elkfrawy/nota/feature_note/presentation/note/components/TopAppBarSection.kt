package com.elkfrawy.nota.feature_note.presentation.note.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.elkfrawy.nota.feature_note.domain.util.NoteOrder
import com.elkfrawy.nota.feature_note.domain.util.OrderType


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ScaffoldSection(
    snackbarHost: SnackbarHostState,
    nestedMenuSelection: MutableState<Boolean>,
    onMenuSelected: (NoteOrder) -> Unit,
    onFabClick: () -> Unit,
    onDeleteAll: ()-> Unit,
    noteBody: @Composable (padding: PaddingValues) -> Unit,
) {


    var menuSelection = remember {
        mutableStateOf(false)
    }
    var noteOrder: MutableState<NoteOrder> = remember {
        mutableStateOf(NoteOrder.Date(OrderType.Descending))
    }


    Scaffold(snackbarHost = { SnackbarHost(snackbarHost)},
        topBar = {
            TopAppBar(title = {
                              Text(text = "Your notes")
            },
                actions = {
                    IconButton(onClick = onDeleteAll) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete All",
                            tint = Color.Red
                        )
                    }

                    IconButton(onClick = { menuSelection.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Sort options")
                    }

                    MainMenu(
                        menuSelection = menuSelection,
                        nestedMenuSelection = nestedMenuSelection
                    ) {
                        noteOrder.value = it
                    }
                    NestedMenu(
                        noteOrder = noteOrder.value,
                        nestedMenuSelection = nestedMenuSelection, onMenuSelected = onMenuSelected
                    )
                })

        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        }

    ) {
        noteBody(it)
    }
}


@Composable
fun MainMenu(
    menuSelection: MutableState<Boolean>,
    nestedMenuSelection: MutableState<Boolean>,
    onMainMenuSelected: (NoteOrder) -> Unit,
) {

    DropdownMenu(
        expanded = menuSelection.value,
        onDismissRequest = { menuSelection.value = false }) {
        DropdownMenuItem(text = {
            Row {
                Text(text = "Date", modifier = Modifier.weight(0.90f))
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "More")
            }
        }, onClick = {
            onMainMenuSelected(NoteOrder.Date(OrderType.Descending))
            menuSelection.value = false
            nestedMenuSelection.value = true
        })

        DropdownMenuItem(text = {
            Row {
                Text(text = "Title", modifier = Modifier.weight(0.90f))
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "More")
            }
        }, onClick = {
            onMainMenuSelected(NoteOrder.Title(OrderType.Descending))
            menuSelection.value = false
            nestedMenuSelection.value = true
        })
    }

}


@Composable
fun NestedMenu(
    noteOrder: NoteOrder,
    nestedMenuSelection: MutableState<Boolean>,
    onMenuSelected: (NoteOrder) -> Unit
) {

    DropdownMenu(
        expanded = nestedMenuSelection.value,
        onDismissRequest = { nestedMenuSelection.value = false }) {
        DropdownMenuItem(
            text = { Text(text = "Descending") },
            onClick = { onMenuSelected(noteOrder.copy(OrderType.Descending)) }
        )
        DropdownMenuItem(
            text = { Text(text = "Ascending") },
            onClick = { onMenuSelected(noteOrder.copy(OrderType.Ascending)) }
        )
    }

}
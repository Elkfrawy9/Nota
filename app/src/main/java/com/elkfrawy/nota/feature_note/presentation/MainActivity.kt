package com.elkfrawy.nota.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elkfrawy.nota.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.elkfrawy.nota.feature_note.presentation.add_edit_note.components.AddEditNoteScreen
import com.elkfrawy.nota.feature_note.presentation.note.NoteViewModel
import com.elkfrawy.nota.feature_note.presentation.note.components.NoteScreen
import com.elkfrawy.nota.feature_note.presentation.util.Screen
import com.elkfrawy.nota.ui.theme.NotaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotaTheme {
                Surface {

                    val controller = rememberNavController()
                    NavHost(navController = controller, startDestination = Screen.NoteScreen.route){

                        composable(Screen.NoteScreen.route){
                            NoteScreen(controller = controller)
                        }

                        composable(Screen.AddEditNoteScreen.route + "?noteId={noteId}&color={color}",
                            arguments = listOf(navArgument("noteId"){
                                type = NavType.IntType
                                defaultValue = -1 }, navArgument("color"){
                                type = NavType.IntType
                                defaultValue = -1
                            })

                        ){
                            AddEditNoteScreen(controller = controller,
                                color = it.arguments?.getInt("color") ?: -1)
                        }

                    }

                }
            }
        }
    }



    @ExperimentalMaterial3Api
    @Preview(showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        NotaTheme {

        }
    }
}
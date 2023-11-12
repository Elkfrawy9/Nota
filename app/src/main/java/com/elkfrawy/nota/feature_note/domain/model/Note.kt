package com.elkfrawy.nota.feature_note.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elkfrawy.nota.ui.theme.BabyBlue
import com.elkfrawy.nota.ui.theme.LightBlue
import com.elkfrawy.nota.ui.theme.LightGreen
import com.elkfrawy.nota.ui.theme.RedOrange
import com.elkfrawy.nota.ui.theme.RedPink
import com.elkfrawy.nota.ui.theme.Violet
import java.util.Date

@Entity()
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
){

    companion object{
        val noteColors = listOf(RedOrange, LightGreen, LightBlue, Violet, BabyBlue, RedPink)
    }

}


class InvalidNoteException(text: String): Exception(text)

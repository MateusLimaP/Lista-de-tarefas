package com.mateuslima.listaafazeres.data.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "tarefa_table")
@Parcelize
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val criada: Long = System.currentTimeMillis(),
    val importante : Boolean = false,
    val completada: Boolean = false
) : Parcelable{
    val criarDataFormatada: String
        get() = DateFormat.getDateTimeInstance().format(criada)
}
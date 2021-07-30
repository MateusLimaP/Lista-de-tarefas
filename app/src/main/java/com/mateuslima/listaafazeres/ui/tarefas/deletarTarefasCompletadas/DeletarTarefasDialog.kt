package com.mateuslima.listaafazeres.ui.tarefas.deletarTarefasCompletadas

import android.content.Context
import androidx.appcompat.app.AlertDialog

class DeletarTarefasDialog private constructor(
    val context: Context,
    val titulo: String,
    val clickConfirmar: () -> Unit
) {



    fun show(){
        val dialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage("Deletar todas tarefas selecionadas?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar"){a,b ->
             clickConfirmar.invoke()
             a.dismiss()
            }
        dialog.show()
    }

    class Builder(
        private val context: Context){

        private lateinit var titulo: String
        fun titulo(titulo: String) = apply { this.titulo = titulo }

        private lateinit var clickConfirmar: () -> Unit
        fun clickConfirmar(clickConfirmar: () -> Unit) = apply { this.clickConfirmar = clickConfirmar }

        fun show() : Any{
            return DeletarTarefasDialog(context, titulo, clickConfirmar).show()
        }
    }


}
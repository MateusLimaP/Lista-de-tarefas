package com.mateuslima.listaafazeres.ui.addtarefa

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateuslima.listaafazeres.data.db.TarefaRepository
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import com.mateuslima.listaafazeres.util.TIPO_ADICIONAR_TAREFA
import com.mateuslima.listaafazeres.util.TIPO_EDITAR_TAREFA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTarefaViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: TarefaRepository
) : ViewModel() {

    private val tarefa = state.get<Tarefa>("tarefa")
    private val tipo = state.get<String>("tipo")

    fun nomeTarefa() : String{
        if (tipo == TIPO_EDITAR_TAREFA)
            return tarefa!!.nome
        return ""
    }

    fun tarefaImportante(): Boolean{
        if (tipo == TIPO_EDITAR_TAREFA)
            return tarefa!!.importante
        return false
    }

    fun dataCriadaVisivel() : Boolean{
        if (tarefa != null)
            return true
        return false
    }

    fun dataCriada() : String{
        if (tipo == TIPO_EDITAR_TAREFA)
            return "Criada: ${tarefa!!.criarDataFormatada}"
        return ""
    }

    fun tituloToolbar(): String{
        if (tipo == TIPO_EDITAR_TAREFA)
            return "Editar Tarefa"
        return "Adicionar Tarefa"
    }

    fun salvarTarefa(nome: String, importante: Boolean, navegacao: () -> Unit){
        viewModelScope.launch {//
            if (tipo == TIPO_EDITAR_TAREFA) {
                repository.atualizarTarefa(tarefa!!.copy(nome = nome, importante = importante))
                navegacao.invoke()
            }
            if (tipo == TIPO_ADICIONAR_TAREFA){
                val novaTarefa = Tarefa(nome = nome, importante = importante)
                repository.addTarefa(novaTarefa)
                navegacao.invoke()
            }
        }
    }

    enum class Navegacao{IR_TELA_PRICIPAL, IR_TELA_SECUNDARIA}



}
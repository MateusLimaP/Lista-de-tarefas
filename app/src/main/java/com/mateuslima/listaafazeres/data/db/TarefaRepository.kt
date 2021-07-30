package com.mateuslima.listaafazeres.data.db

import androidx.lifecycle.LiveData
import com.mateuslima.listaafazeres.data.db.dao.TarefaDao
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import com.mateuslima.listaafazeres.ui.tarefas.TarefasViewModel
import com.mateuslima.listaafazeres.ui.tarefas.TarefasViewModel.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TarefaRepository @Inject constructor(
   private val dao: TarefaDao
) {

    suspend fun addTarefa(tarefa: Tarefa){
        dao.addTarefa(tarefa)
    }

    suspend fun atualizarTarefa(tarefa: Tarefa){
        dao.atualizarTarefa(tarefa)
    }

    suspend fun deletarTarefa(tarefa: Tarefa){
        dao.deletarTarefa(tarefa)
    }

    suspend fun deletarTarefasCompletas(){
        dao.deletarTarefasCompletas()
    }



    fun getTarefaNome(pesquisa: String, hide: Boolean) : Flow<List<Tarefa>>{
        return  dao.getTarefaPorNome(pesquisa, hide)
    }
    fun getTarefaData(pesquisa: String, hide: Boolean) : Flow<List<Tarefa>>{
        return dao.getTarefaPorData(pesquisa, hide)
    }





}
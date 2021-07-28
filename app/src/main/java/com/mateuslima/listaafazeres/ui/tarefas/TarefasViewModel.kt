package com.mateuslima.listaafazeres.ui.tarefas

import androidx.lifecycle.*
import com.mateuslima.listaafazeres.data.db.TarefaRepository
import com.mateuslima.listaafazeres.data.db.dao.TarefaDao
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import com.mateuslima.listaafazeres.data.db.preference.PreferencesManager
import com.mateuslima.listaafazeres.data.db.preference.PreferencesManager.Organizar.*
import com.mateuslima.listaafazeres.util.ORGANIZAR_POR_DATA
import com.mateuslima.listaafazeres.util.ORGANIZAR_POR_NOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TarefasViewModel @Inject constructor(
    private val repository: TarefaRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    private val pesquisa = MutableStateFlow("")

    private var tarefa = combine(pesquisa, preferencesManager.tarefasPreference){ a, b ->
        Wrapper(a, b)
    }.flatMapLatest {  (search, preference) ->
        when(preference.ordem){
            POR_NOME -> repository.getTarefaNome(search, preference.hideTask)
            POR_DATA -> repository.getTarefaData(search, preference.hideTask)

        }

    }


    fun pesquisarTarefa(search: String){
        pesquisa.value = search }

    fun organizarPorNome(){
        viewModelScope.launch(IO) { preferencesManager.updateOrdemTarefas(POR_NOME) }}

    fun organizarPorData(){
        viewModelScope.launch(IO) { preferencesManager.updateOrdemTarefas(POR_DATA) }}

    fun esconderCompletos(hide: Boolean){
        viewModelScope.launch(IO) { preferencesManager.updateEsconderTarefasCompletas(hide) }
    }
    fun atualizarTarefa(tarefa: Tarefa, isChecked: Boolean){
        viewModelScope.launch(IO) { repository.atualizarTarefa(tarefa.copy(completada = isChecked)) }
    }

    fun deletarTarefa(tarefa: Tarefa){
        viewModelScope.launch(IO) { repository.deletarTarefa(tarefa)}
    }

    fun getListaTarefa()  = tarefa.asLiveData()

    suspend fun marcarTarefaMenu() : Boolean{
        return preferencesManager.tarefasPreference.first().hideTask
    }



    data class Wrapper<T1, T2>(val v1: T1, val v2: T2)
}
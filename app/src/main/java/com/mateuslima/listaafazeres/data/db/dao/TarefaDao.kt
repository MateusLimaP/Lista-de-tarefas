package com.mateuslima.listaafazeres.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTarefa(tarefa: Tarefa)

    @Delete
    suspend fun deletarTarefa(tarefa: Tarefa)

    @Update
    suspend fun atualizarTarefa(tarefa: Tarefa)

    @Query("SELECT * FROM tarefa_table WHERE (completada != :hideComplete OR completada = 0) AND nome LIKE '%' || :pesquisa || '%' ORDER BY importante DESC, nome")
    fun getTarefaPorNome(pesquisa: String, hideComplete: Boolean) : Flow<List<Tarefa>>

    @Query("SELECT * FROM tarefa_table WHERE (completada != :hideComplete OR completada = 0) AND nome LIKE '%' || :pesquisa || '%' ORDER BY importante DESC, criada")
    fun getTarefaPorData(pesquisa: String, hideComplete: Boolean) : Flow<List<Tarefa>>

    @Query("DELETE FROM tarefa_table WHERE completada = 1")
    suspend fun deletarTarefasCompletas()



}
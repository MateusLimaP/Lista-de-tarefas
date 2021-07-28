package com.mateuslima.listaafazeres.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mateuslima.listaafazeres.data.db.dao.TarefaDao
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import com.mateuslima.listaafazeres.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Tarefa::class], version = 2)
abstract class TarefaDatabase : RoomDatabase() {

    abstract fun tarefaDao() : TarefaDao


    class Callback @Inject constructor(
        val dao: Provider<TarefaDao>,
        @ApplicationScope val applicationScope : CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)


            applicationScope.launch {
                dao.get().addTarefa(Tarefa(nome = "Lavar Louça", completada = true))
                dao.get().addTarefa(Tarefa(nome = "Ir ao Supermercado", importante = true))
                dao.get().addTarefa(Tarefa(nome = "Arrumar Bicicleta"))
                dao.get().addTarefa(Tarefa(nome = "Ir a Faculdade", importante = true))
                dao.get().addTarefa(Tarefa(nome = "Jogar PS4"))
                dao.get().addTarefa(Tarefa(nome = "Tarefa 1"))
                dao.get().addTarefa(Tarefa(nome = "Tarefa 2"))
                dao.get().addTarefa(Tarefa(nome = "Tarefa 3"))
            }


        }
    }


    class Migrations @Inject constructor(){
        val MIGRATION_1_2= object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }

    }




}
package com.mateuslima.listaafazeres.di.module

import android.app.Application
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.room.Room
import com.mateuslima.listaafazeres.adapter.ItemTouchHelperAdapter
import com.mateuslima.listaafazeres.data.db.TarefaDatabase
import com.mateuslima.listaafazeres.data.db.TarefaDatabase.*
import com.mateuslima.listaafazeres.data.db.dao.TarefaDao
import com.mateuslima.listaafazeres.di.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideDatabase(application: Application,
                          m: Migrations,
                          callback: Callback) : TarefaDatabase{
        return Room.databaseBuilder(application, TarefaDatabase::class.java, "db_tarefa")
            .addMigrations(m.MIGRATION_1_2)
            .addCallback(callback)
            .build()
    }


    @Singleton
    @Provides
    fun provideDao(db: TarefaDatabase): TarefaDao{
        return db.tarefaDao()
    }

    @ApplicationScope
    @Singleton
    @Provides
    fun provideCoroutineIo() : CoroutineScope {
       return CoroutineScope(SupervisorJob())
    }


}
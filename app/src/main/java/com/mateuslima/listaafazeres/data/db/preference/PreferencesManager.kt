package com.mateuslima.listaafazeres.data.db.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.mateuslima.listaafazeres.data.db.preference.PreferencesManager.Organizar.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object { // preferences key
        private val ESCONDER_TAREFAS_COMPLETADAS = booleanPreferencesKey("hide")
        private val ORDEM_TAREFA = stringPreferencesKey("organizar")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("prefs")

    val tarefasPreference = context.dataStore.data
        .map { preferences ->
            val ordemOrganizar = valueOf(preferences[ORDEM_TAREFA] ?: POR_NOME.name)
            val esconderTarefa = preferences[ESCONDER_TAREFAS_COMPLETADAS] ?: false
            PreferenciasFiltro(ordemOrganizar, esconderTarefa)
        }

    suspend fun updateEsconderTarefasCompletas(hide: Boolean) {
        context.dataStore.edit { config ->
            config[ESCONDER_TAREFAS_COMPLETADAS] = hide
        }

    }

    suspend fun updateOrdemTarefas(ordem: Organizar){
        context.dataStore.edit { config ->
            config[ORDEM_TAREFA] = ordem.name
        }
    }

    enum class Organizar{POR_NOME, POR_DATA}

    data class PreferenciasFiltro<T1, T2>(val ordem: T1, val hideTask: T2)
}
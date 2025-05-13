package com.example.gamebox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebox.model.CollectionEntry
import com.example.gamebox.model.GameEntry
import com.example.gamebox.repository.LibraryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private val repo = LibraryRepository

    // Flows de Firebase en StateFlows para Compose
    val games: StateFlow<List<GameEntry>> = repo.observeGames()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val collections: StateFlow<List<CollectionEntry>> = repo.observeCollections()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Contadores
    val totalGames: StateFlow<Int> = games.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalCollections: StateFlow<Int> = collections.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // % completado: (colecciones sin totalInSaga) se ignoran en el porcentaje
    val percentComplete = combine(games, collections) { games, cols ->
        // Sólo colecciones que tienen un objetivo definido:
        val targetCols = cols.filter { it.totalInSaga != null }
        if (targetCols.isEmpty()) 0f else {
            val done = targetCols.count { col ->
                val ti = col.totalInSaga!!
                games.count { it.collectionId == col.collectionId } >= ti
            }
            done.toFloat() / targetCols.size
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)


    // Funciones para modificar
    fun createCollection(name: String, totalInSaga: Int? = null, onDone: (String) -> Unit) {
        viewModelScope.launch {
            val newId = repo.addCollection(name, totalInSaga)
            onDone(newId)
        }
    }

    fun addGameToCollection(game: GameEntry) {
        viewModelScope.launch {
            repo.addGame(game)
        }
    }

    fun updateCollection(colId: String, newName: String, newTotal: Int?, onDone: () -> Unit) {
        viewModelScope.launch {
            repo.updateCollection(colId, newName, newTotal)
            onDone()
        }
    }

    fun deleteCollection(colId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repo.deleteCollection(colId)
            onDone()
        }
    }

    fun deleteGame(docId: String) {
        viewModelScope.launch {
            repo.deleteGame(docId)
        }
    }
}

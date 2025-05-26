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
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val collections: StateFlow<List<CollectionEntry>> = repo.observeCollections()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Contadores
    val totalGames: StateFlow<Int> = games.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalCollections: StateFlow<Int> = collections.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // % completado: (colecciones sin totalInSaga) se ignoran en el porcentaje
    val percentComplete: StateFlow<Float> = combine(games, collections) { games, cols ->
        if (cols.isEmpty()) 0f else {
            val done = cols.count { col ->
                val have = games.count { it.collectionId == col.collectionId }
                col.totalInSaga?.let { have >= it } ?: (have > 0)
            }
            done.toFloat() / cols.size
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,  // <–– aquí
        initialValue = 0f
    )

    // --- Funciones para colecciones y juegos ---

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

    // --- NUEVO: Funciones para logros ---

    /**
     * Marca o desmarca un logro en un juego concreto.
     *
     * @param gameDocId        El ID del documento GameEntry (docId)
     * @param achievementName  Nombre identificador del logro
     * @param achieved         True para marcar como conseguido, false para desmarcar
     */
    fun setAchievement(
        gameDocId: String,
        achievementName: String,
        achieved: Boolean
    ) {
        viewModelScope.launch {
            repo.setAchievementState(gameDocId, achievementName, achieved)
        }
    }

    /**
     * Observa en tiempo real el estado de todos los logros de un juego.
     * Devuelve un StateFlow de Map<nombreDelLogro, estadoBooleano>.
     *
     * @param gameDocId  El ID del documento GameEntry (docId)
     */
    fun observeAchievements(gameDocId: String): StateFlow<Map<String, Boolean>> {
        return repo.observeAchievementStates(gameDocId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyMap()
            )
    }
}

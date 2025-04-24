package com.example.gamebox.steam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebox.steam.model.AppInfo
import com.example.gamebox.steam.repository.SteamRepository
import com.example.gamebox.epic.model.EpicAppInfo
import com.example.gamebox.epic.repository.EpicRepository
import com.example.gamebox.epic.repository.SEARCH_QUERY
import com.example.gamebox.epic.model.GraphQLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * ViewModel unificado para búsquedas en Steam y Epic Store.
 */
class SearchViewModel : ViewModel() {
    private val steamRepo = SteamRepository()
    private val epicRepo = EpicRepository()

    var query by mutableStateOf("")
        private set

    // null = no buscado; true/false = encontrado o no
    var exists by mutableStateOf<Boolean?>(null)
        private set

    // Puede contener una o dos URLs de cabecera
    var headerUrls by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedItem by mutableStateOf<ResultItem?>(null)
        private set

    // Carga de apps de Steam al iniciar
    private var steamApps: List<AppInfo> = emptyList()

    // Sugerencias de búsqueda: Steam + Epic, sin duplicados
    var suggestions by mutableStateOf<List<ResultItem>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            steamApps = steamRepo.fetchAllApps()
        }
    }

    /**
     * Actualiza la consulta y refresca sugerencias.
     */
    fun onQueryChange(text: String) {
        query = text
        exists = null
        headerUrls = emptyList()
        selectedItem = null

        viewModelScope.launch {
            // 1) Obtén hasta 5 sugerencias de Steam
            val steamSug = steamApps
                .filter { it.name.contains(text, ignoreCase = true) }
                .take(5)
                .map { ResultItem.Steam(it) }

            // 2) Obtén hasta 5 sugerencias de Epic
            val epicSug = epicRepo.searchApps(text)
                .take(5)
                .map { ResultItem.Epic(it) }

            // 3) Agrupa por nombre para evitar duplicados
            suggestions = (steamSug + epicSug)
                .groupBy { item ->
                    when (item) {
                        is ResultItem.Steam -> item.info.name
                        is ResultItem.Epic  -> item.info.title
                        is ResultItem.Both  -> item.steam.name
                    }
                }
                .map { (_, items) ->
                    val steamItem = items.filterIsInstance<ResultItem.Steam>().firstOrNull()
                    val epicItem  = items.filterIsInstance<ResultItem.Epic>().firstOrNull()
                    when {
                        steamItem != null && epicItem != null ->
                            ResultItem.Both(steamItem.info, epicItem.info)
                        steamItem != null -> steamItem
                        epicItem  != null -> epicItem
                        else -> items.first()
                    }
                }
        }
    }

    /**
     * Selecciona un ítem de la lista de sugerencias, detectando si existe en ambas tiendas.
     */
    fun selectItem(item: ResultItem) {
        // Fija el query al nombre exacto
        query = when (item) {
            is ResultItem.Steam -> item.info.name
            is ResultItem.Epic  -> item.info.title
            is ResultItem.Both  -> item.steam.name
        }
        exists = true

        viewModelScope.launch {
            // Determina si el juego está en ambas tiendas
            val finalItem = when (item) {
                is ResultItem.Steam -> {
                    // Busca en Epic por título exacto
                    val epicMatch = epicRepo.searchApps(item.info.name)
                        .firstOrNull { it.title.equals(item.info.name, ignoreCase = true) }
                    if (epicMatch != null) ResultItem.Both(item.info, epicMatch) else item
                }
                is ResultItem.Epic -> {
                    // Busca en Steam por nombre exacto
                    val steamMatch = steamApps
                        .firstOrNull { it.name.equals(item.info.title, ignoreCase = true) }
                    if (steamMatch != null) ResultItem.Both(steamMatch, item.info) else item
                }
                is ResultItem.Both -> item
            }

            selectedItem = finalItem

            // Prepara las URLs de cabecera
            headerUrls = when (val sel = finalItem) {
                is ResultItem.Steam -> listOf(
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/${sel.info.appid}/header.jpg"
                )
                is ResultItem.Epic -> listOf(sel.info.imageUrl)
                is ResultItem.Both -> listOf(
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/${sel.steam.appid}/header.jpg",
                    sel.epic.imageUrl
                )
            }
        }
    }

    /**
     * Búsqueda manual exacta (opcional).
     */
    fun searchExact() {
        viewModelScope.launch {
            // Similar a selectItem pero usando solo coincidencias exactas
            val steamMatch = steamApps
                .firstOrNull { it.name.equals(query, ignoreCase = true) }
            val epicMatch  = epicRepo.searchApps(query)
                .firstOrNull { it.title.equals(query, ignoreCase = true) }

            val sel = when {
                steamMatch != null && epicMatch != null -> ResultItem.Both(steamMatch, epicMatch)
                steamMatch != null -> ResultItem.Steam(steamMatch)
                epicMatch  != null -> ResultItem.Epic(epicMatch)
                else -> null
            }

            selectedItem = sel
            exists = sel != null
            headerUrls = sel?.let {
                when (it) {
                    is ResultItem.Steam -> listOf(
                        "https://cdn.cloudflare.steamstatic.com/steam/apps/${it.info.appid}/header.jpg"
                    )
                    is ResultItem.Epic -> listOf(it.info.imageUrl)
                    is ResultItem.Both -> listOf(
                        "https://cdn.cloudflare.steamstatic.com/steam/apps/${it.steam.appid}/header.jpg",
                        it.epic.imageUrl
                    )
                }
            } ?: emptyList()
        }
    }

    /**
     * Representa un resultado de búsqueda.
     */
    sealed class ResultItem {
        data class Steam(val info: AppInfo)   : ResultItem()
        data class Epic(val info: EpicAppInfo) : ResultItem()
        data class Both(val steam: AppInfo, val epic: EpicAppInfo) : ResultItem()
    }
}

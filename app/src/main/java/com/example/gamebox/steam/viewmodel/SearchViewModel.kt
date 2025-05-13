package com.example.gamebox.steam.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gamebox.epic.model.EpicAppInfo
import com.example.gamebox.epic.repository.EpicRepository
import com.example.gamebox.steam.model.AppInfo
import com.example.gamebox.steam.model.SteamAppDetails
import com.example.gamebox.steam.repository.SteamRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val steamRepo = SteamRepository
    private val epicRepo  = EpicRepository()

    var query by mutableStateOf("")
        private set

    var suggestions by mutableStateOf<List<ResultItem>>(emptyList())
        private set

    var selectedItem by mutableStateOf<ResultItem?>(null)
        private set

    var headerUrls by mutableStateOf<List<String>>(emptyList())
        private set

    var steamPrice by mutableStateOf<String?>(null)
        private set

    var epicPrice by mutableStateOf<String?>(null)
        private set

    /**
     * Al cambiar texto, recarga sugerencias de cada tienda.
     * Atrapa errores HTTP/parseo para que no crashee.
     */
    fun onQueryChange(text: String) {
        query = text
        suggestions = emptyList()
        selectedItem = null
        headerUrls = emptyList()
        steamPrice = null
        epicPrice = null

        viewModelScope.launch {
            // 1) Steam
            val steamInfos = try {
                steamRepo.searchApps(text)
            } catch (e: Exception) {
                Log.w("SearchVM", "Error fetching Steam suggestions", e)
                emptyList<AppInfo>()
            }
            val steamItems = steamInfos.take(5).map { ResultItem.Steam(it) }

            // 2) Epic
            val epicInfos = try {
                epicRepo.searchApps(text)
            } catch (e: Exception) {
                Log.w("SearchVM", "Error fetching Epic suggestions", e)
                emptyList<EpicAppInfo>()
            }
            val epicItems = epicInfos.take(5).map { ResultItem.Epic(it) }

            // 3) Combina y deduplica por título
            suggestions = (steamItems + epicItems)
                .groupBy {
                    when (it) {
                        is ResultItem.Steam -> it.info.name
                        is ResultItem.Epic  -> it.info.title
                        is ResultItem.Both  -> it.steam.name
                    }
                }
                .map { (_, items) ->
                    val s = items.filterIsInstance<ResultItem.Steam>().firstOrNull()
                    val e = items.filterIsInstance<ResultItem.Epic>().firstOrNull()
                    when {
                        s != null && e != null -> ResultItem.Both(s.info, e.info)
                        s != null              -> s
                        e != null              -> e
                        else                   -> items.first()
                    }
                }
        }
    }

    /**
     * Cuando el usuario selecciona un juego, carga cabeceras e intenta
     * sacar el precio de Steam + usa el price que venga en EpicAppInfo.
     */
    fun selectItem(item: ResultItem) {
        selectedItem = item
        headerUrls = emptyList()
        steamPrice = null
        epicPrice = null

        viewModelScope.launch {
            // cabeceras
            headerUrls = when (item) {
                is ResultItem.Steam -> listOf(
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.info.appid}/header.jpg"
                )
                is ResultItem.Epic  -> listOf(item.info.imageUrl)
                is ResultItem.Both  -> listOf(
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.steam.appid}/header.jpg",
                    item.epic.imageUrl
                )
            }

            // precio Steam
            val steamId = when (item) {
                is ResultItem.Steam -> item.info.appid
                is ResultItem.Both  -> item.steam.appid
                else                -> null
            }
            if (steamId != null) {
                try {
                    val details: SteamAppDetails? = steamRepo.fetchAppDetails(steamId)
                    steamPrice = details?.data?.priceOverview?.finalFormatted ?: "—"
                } catch (e: Exception) {
                    Log.w("SearchVM", "Error fetching Steam price", e)
                    steamPrice = "—"
                }
            }

            // precio Epic (ya viene en EpicAppInfo.price)
            epicPrice = when (item) {
                is ResultItem.Epic -> item.info.price ?: "—"
                is ResultItem.Both -> item.epic.price  ?: "—"
                else               -> null
            }
        }
    }

    sealed class ResultItem {
        data class Steam(val info: AppInfo)               : ResultItem()
        data class Epic(val info: EpicAppInfo)            : ResultItem()
        data class Both(val steam: AppInfo, val epic: EpicAppInfo) : ResultItem()
    }
}

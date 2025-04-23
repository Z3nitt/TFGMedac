package com.example.gamebox.steam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gamebox.steam.model.AppInfo
import com.example.gamebox.steam.repository.SteamRepository

class SearchViewModel : ViewModel() {
    private val repo = SteamRepository()

    var query by mutableStateOf("")
        private set

    var exists by mutableStateOf<Boolean?>(null)
        private set

    var headerUrl by mutableStateOf<String?>(null)
        private set

    private var apps: List<AppInfo> = emptyList()

    init {
        viewModelScope.launch {
            apps = repo.fetchAllApps()
        }
    }

    fun onQueryChange(text: String) {
        query = text
        exists = null
        headerUrl = null
    }

    fun search() {
        // Búsqueda exacta antes que parcial
        val match = apps.find { it.name.equals(query, ignoreCase = true) }
            ?: apps.find { it.name.contains(query, ignoreCase = true) }

        if (match != null) {
            exists = true
            // Patrón de URL oficial para header image
            headerUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/${match.appid}/header.jpg"
        } else {
            exists = false
            headerUrl = null
        }
    }
    // En SearchViewModel.kt
    val suggestions: List<AppInfo>
        get() = apps
            .filter { it.name.contains(query, ignoreCase = true) }
            .take(5)

}


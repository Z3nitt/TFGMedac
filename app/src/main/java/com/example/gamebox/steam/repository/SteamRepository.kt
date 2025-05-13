package com.example.gamebox.steam.repository

import com.example.gamebox.steam.model.AppInfo
import com.example.gamebox.steam.network.SteamApi

class SteamRepository {
    // Caché en memoria para no volver a bajar toda la lista
    private var cache: List<AppInfo>? = null

    // Lanza la petición la primera vez y guarda en cache
    suspend fun fetchAllApps(): List<AppInfo> {
        cache?.let { return it }
        val response = SteamApi.service.getAppList()
        val apps = response.applist.apps
        cache = apps
        return apps
    }
}

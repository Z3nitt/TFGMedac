package com.example.gamebox.steam.network

import retrofit2.http.GET
import com.example.gamebox.steam.model.AppListResponse

interface SteamApiService {
    @GET("ISteamApps/GetAppList/v2/")
    suspend fun getAppList(): AppListResponse
}

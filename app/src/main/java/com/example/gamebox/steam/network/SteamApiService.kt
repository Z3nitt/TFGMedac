package com.example.gamebox.steam.network

import retrofit2.http.GET
import com.example.gamebox.steam.model.AppListResponse
import com.example.gamebox.steam.network.model.SchemaResponse
import retrofit2.http.Query

interface SteamApiService {
    @GET("ISteamApps/GetAppList/v2/")
    suspend fun getAppList(): AppListResponse

    /**
     * Llama al endpoint:
     * https://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/
     * para obtener la lista de logros de un juego.
     *
     * @param key   Tu Steam Web API key.
     * @param appid El Steam AppID del juego.
     */
    @GET("ISteamUserStats/GetSchemaForGame/v2/")
    suspend fun getSchemaForGame(
        @Query("key") key: String,
        @Query("appid") appid: Int
    ): SchemaResponse
}

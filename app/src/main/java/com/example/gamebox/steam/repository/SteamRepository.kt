// SteamRepository.kt
package com.example.gamebox.steam.repository

import com.example.gamebox.steam.model.AppInfo
import com.example.gamebox.steam.model.SteamAppDetails
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private interface SteamApi {
    @GET("/api/appdetails")
    suspend fun getAppDetails(
        @Query("appids") appId: Int
    ): Map<String, SteamAppDetails>

    @GET("/api/storesearch")
    suspend fun search(
        @Query("cc") country: String = "us",
        @Query("l") lang: String = "en",
        @Query("term") term: String
    ): SearchResponse
}

data class SearchResponse(val items: List<SearchItem>)
data class SearchItem(
    val id: Int,
    val name: String,
    @Json(name = "tiny_image") val tinyImage: String
)

object SteamRepository {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val client = OkHttpClient.Builder().build()

    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SteamApi::class.java)

    /** Busca juegos por texto en Steam Store */
    suspend fun searchApps(query: String): List<AppInfo> = withContext(Dispatchers.IO) {
        try {
            api.search(term = query)
                .items
                .map { AppInfo(appid = it.id, name = it.name) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /** Obtiene detalles (incluyendo precio) de un juego concreto */
    suspend fun fetchAppDetails(appid: Int): SteamAppDetails? = withContext(Dispatchers.IO) {
        try {
            api.getAppDetails(appid).values.firstOrNull()
        } catch (_: Exception) {
            null
        }
    }
}

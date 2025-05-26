// SteamStatsRepository.kt
package com.example.gamebox.steam.repository

import com.example.gamebox.steam.network.SteamApiService
import com.example.gamebox.steam.network.model.AchievementSchema
import com.example.gamebox.steam.network.model.SchemaResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SteamStatsRepository(
    private val service: SteamApiService,
    private val apiKey: String
) {
    suspend fun fetchAchievementsList(appId: Int): List<AchievementSchema> =
        withContext(Dispatchers.IO) {
            val resp: SchemaResponse = service.getSchemaForGame(apiKey, appId)
            resp.game
                ?.stats
                ?.achievements
                .orEmpty()
        }
}

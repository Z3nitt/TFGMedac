package com.example.gamebox.steam.network.model

import com.squareup.moshi.Json

data class SchemaResponse(
    @Json(name = "game") val game: GameSchema?
)

data class GameSchema(
    @Json(name = "availableGameStats") val stats: GameStats?
)

data class GameStats(
    val achievements: List<AchievementSchema>?
)

data class AchievementSchema(
    val name: String,
    @Json(name = "displayName") val title: String
)

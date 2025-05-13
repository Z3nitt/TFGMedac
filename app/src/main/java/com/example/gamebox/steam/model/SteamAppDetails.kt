package com.example.gamebox.steam.model

import com.squareup.moshi.Json

/** Modelo para parsear la respuesta de https://store.steampowered.com/api/appdetails */
data class SteamAppDetails(
    @Json(name = "data") val data: AppData?
)

data class AppData(
    @Json(name = "price_overview") val priceOverview: PriceOverview?
)

data class PriceOverview(
    @Json(name = "final_formatted") val finalFormatted: String
)


package com.example.gamebox.steam.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SteamApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.steampowered.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val service: SteamApiService =
        retrofit.create(SteamApiService::class.java)
}

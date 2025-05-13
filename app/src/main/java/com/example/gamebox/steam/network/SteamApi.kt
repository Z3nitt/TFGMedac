package com.example.gamebox.steam.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SteamApi {
    // 1) Construye un Moshi con KotlinJsonAdapterFactory
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // 2) Usa ese Moshi en Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.steampowered.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: SteamApiService =
        retrofit.create(SteamApiService::class.java)
}

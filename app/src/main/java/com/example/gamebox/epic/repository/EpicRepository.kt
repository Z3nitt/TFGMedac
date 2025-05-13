package com.example.gamebox.epic.repository

import com.example.gamebox.epic.model.EpicAppInfo
import com.example.gamebox.epic.model.GraphQLRequest
import com.example.gamebox.epic.model.GraphQLResponse
import com.example.gamebox.epic.model.EpicStoreData
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class EpicRepository {
    private val service: EpicGraphQLService

    init {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://www.epicgames.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(EpicGraphQLService::class.java)
    }

    suspend fun searchApps(query: String, start: Int = 0, count: Int = 5): List<EpicAppInfo> =
        withContext(Dispatchers.IO) {
            val variables = mapOf(
                "keywords" to query,
                "start"    to start,
                "count"    to count
            )
            val body = GraphQLRequest(SEARCH_QUERY, variables)
            val resp: GraphQLResponse<EpicStoreData> = service.searchStore(body)
            val elems = resp.data
                ?.Catalog
                ?.searchStore
                ?.elements
                .orEmpty()

            elems.map { el ->
                val img = el.keyImages
                    .firstOrNull { it.type == "OfferImageTall" }
                    ?.url
                    ?: el.keyImages.firstOrNull()?.url.orEmpty()
                EpicAppInfo(id = el.id, title = el.title, imageUrl = img)
            }
        }
}

// src/main/java/com/example/gamebox/epic/repository/EpicRepository.kt
package com.example.gamebox.epic.repository

import android.util.Log
import com.example.gamebox.epic.model.EpicAppInfo
import com.example.gamebox.epic.model.GraphQLRequest
import com.example.gamebox.epic.model.GraphQLResponse
import com.example.gamebox.epic.model.EpicStoreData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.Locale

class EpicRepository {
    private val service: EpicGraphQLService

    init {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .header("Origin", "https://graphql.epicgames.com")
                    .build()
                chain.proceed(req)
            }
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://graphql.epicgames.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(EpicGraphQLService::class.java)
    }

    /**
     * Busca juegos en Epic Store.
     */
    suspend fun searchApps(
        query: String,
        start: Int = 0,
        count: Int = 5
    ): List<EpicAppInfo> = withContext(Dispatchers.IO) {
        val locale  = Locale.getDefault().toLanguageTag()
        val country = Locale.getDefault().country

        val variables = mapOf(
            "keywords" to query,
            "start"    to start,
            "count"    to count,
            "country"  to country,
            "locale"   to locale
        )

        val body = GraphQLRequest(
            operationName = "searchStore",
            query         = EpicQueries.SEARCH_QUERY,
            variables     = variables
        )

        val resp: GraphQLResponse<EpicStoreData> = service.searchStore(body)

        resp.data
            ?.Catalog
            ?.searchStore
            ?.elements
            .orEmpty()
            .map { el ->
                // Imagen principal
                val img = el.keyImages
                    .firstOrNull { it.type == "DieselStoreFrontTall" }
                    ?.url
                    ?: el.keyImages.firstOrNull()?.url.orEmpty()

                // Precio anidado
                val fmt = el.price
                    ?.totalPrice
                    ?.fmtPrice
                val priceText = fmt?.discountPrice ?: fmt?.originalPrice

                EpicAppInfo(
                    id       = el.id,
                    title    = el.title,
                    imageUrl = img,
                    price    = priceText
                )
            }
    }
}

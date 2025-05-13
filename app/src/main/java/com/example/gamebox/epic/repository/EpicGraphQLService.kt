// EpicGraphQLService.kt
package com.example.gamebox.epic.repository

import com.example.gamebox.epic.model.EpicStoreData
import com.example.gamebox.epic.model.GraphQLRequest
import com.example.gamebox.epic.model.GraphQLResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EpicGraphQLService {
    @Headers("Content-Type: application/json")
    @POST("graphql")
    suspend fun searchStore(@Body request: GraphQLRequest): GraphQLResponse<EpicStoreData>
}

package com.example.gamebox.epic.repository

import com.example.gamebox.epic.model.EpicStoreData
import com.example.gamebox.epic.model.GraphQLRequest
import com.example.gamebox.epic.model.GraphQLResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface EpicGraphQLService {
    @POST("graphql")
    suspend fun searchStore(@Body body: GraphQLRequest): GraphQLResponse<EpicStoreData>
}

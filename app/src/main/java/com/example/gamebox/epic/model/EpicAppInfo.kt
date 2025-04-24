package com.example.gamebox.epic.model

data class GraphQLRequest(
    val query: String,
    val variables: Map<String, @JvmSuppressWildcards Any?>
)
data class GraphQLResponse<T>(val data: T?)

// Respuesta Epic Store
data class EpicStoreData(val Catalog: CatalogPayload)
data class CatalogPayload(val searchStore: SearchStorePayload)
data class SearchStorePayload(
    val elements: List<StoreElement>
)
data class StoreElement(
    val title: String,
    val id: String,
    val namespace: String,
    val keyImages: List<KeyImage>
)
data class KeyImage(
    val type: String,
    val url: String
)

// Tu modelo de app
data class EpicAppInfo(
    val id: String,
    val title: String,
    val imageUrl: String
)

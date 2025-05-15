package com.example.gamebox.epic.model

import com.squareup.moshi.Json

/** Datos recibidos de la respuesta GraphQL de Epic Store */
data class EpicStoreData(val Catalog: CatalogPayload)
data class CatalogPayload(val searchStore: SearchStorePayload)
data class SearchStorePayload(val elements: List<StoreElement>)

// Se añade campo price

data class StoreElement(
    val title: String,
    val id: String,
    val namespace: String,
    val keyImages: List<KeyImage>,
    val price: PricePayload?    // ahora payload anidado
)

data class KeyImage(
    val type: String,
    val url: String
)

data class PricePayload(
    val totalPrice: TotalPricePayload
)

data class TotalPricePayload(
    val fmtPrice: FormattedPricePayload?,
    val discount: Int?
)

data class FormattedPricePayload(
    val discountPrice: String?,
    val originalPrice: String?,
    //val discountPercent: String?
)

/** Representa la estructura de precio en la respuesta */
data class Price(
    @Json(name = "totalPrice") val totalPrice: Money,
    @Json(name = "discountPrice") val discountPrice: Money? = null
)

data class Money(
    @Json(name = "fmtPrice") val fmtPrice: String
)

data class EpicAppInfo(
    val id: String,
    val title: String,
    val imageUrl: String,
    val price: String?  // formato final
)

data class EpicDiscountedGame(
    val id: String,
    val title: String,
    val imageUrl: String,
    val originalPrice: String?,
    val finalPrice: String?,
    val discountPercent: String?
)

data class GraphQLRequest(
    val operationName: String,
    val query: String,
    val variables: Map<String, @JvmSuppressWildcards Any?>
)

data class GraphQLResponse<T>(
    val data: T? = null,
    val errors: List<GraphQLError>? = null
)

/** Modelo para cada error GraphQL */
data class GraphQLError(
    val message: String
)




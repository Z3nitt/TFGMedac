package com.example.gamebox.epic.repository

/** Query GraphQL para búsqueda */
object EpicQueries {
    const val SEARCH_QUERY = """
    query searchStore(
      ${"$"}keywords: String!,
      ${"$"}start: Int!,
      ${"$"}count: Int!,
      ${"$"}country: String!,
      ${"$"}locale: String!,
      ${"$"}onSale: Boolean!
    ) {
      Catalog {
        searchStore(
          keywords: ${"$"}keywords,
          start: ${"$"}start,
          count: ${"$"}count,
          country: ${"$"}country,
          locale: ${"$"}locale,
          onSale: ${"$"}onSale
        ) {
          elements {
            id
            title
            namespace
            keyImages { type url }
            price(country: ${"$"}country) {
              totalPrice {
                discount
                fmtPrice(locale: ${"$"}locale) {
                  discountPrice
                  originalPrice
                }
              }
            }
          }
        }
      }
    }
    """
}


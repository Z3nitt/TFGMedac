package com.example.gamebox.epic.repository

/** Query GraphQL para búsqueda */
object EpicQueries {
    const val SEARCH_QUERY = """
    query searchStore(
      ${"$"}keywords: String!,
      ${"$"}start: Int!,
      ${"$"}count: Int!,
      ${"$"}country: String!,
      ${"$"}locale: String!
    ) {
      Catalog {
        searchStore(
          keywords: ${"$"}keywords,
          start: ${"$"}start,
          count: ${"$"}count,
          country: ${"$"}country,
          locale: ${"$"}locale
        ) {
          elements {
            id
            title
            namespace
            keyImages { type url }
            price(country: ${"$"}country) {
              totalPrice {
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


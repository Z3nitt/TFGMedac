package com.example.gamebox.epic.repository

internal const val SEARCH_QUERY = """
query searchStoreQuery(${"$"}keywords:String,${"$"}start:Int,${"$"}count:Int){
  Catalog {
    searchStore(keywords:${"$"}keywords,start:${"$"}start,count:${"$"}count){
      elements {
        title
        id
        namespace
        keyImages {
          type
          url
        }
      }
      paging {
        count
        total
      }
    }
  }
}
"""

package com.example.gamebox.steam.model

// 1) Clase raíz que coincide con la estructura JSON:
//    { "applist": { "apps": [ ... ] } }
data class AppListResponse(
    val applist: Applist        // "applist" en el JSON
)

// 2) Clase intermedia que agrupa la lista
data class Applist(
    val apps: List<AppInfo>     // "apps" es un array de objetos
)

// 3) Cada objeto de la lista con sus dos campos
data class AppInfo(
    val appid: Int,             // "appid" en el JSON
    val name: String            // "name" en el JSON
)
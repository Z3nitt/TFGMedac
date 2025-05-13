package com.example.gamebox.model

import com.google.firebase.Timestamp

data class CollectionEntry(
    val collectionId: String = "",
    val name: String = "",
    val totalInSaga: Int? = null,
    val createdAt: Timestamp = Timestamp.now()
)
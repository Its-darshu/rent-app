package com.nammayantra.app.data.model

import com.google.firebase.firestore.DocumentId

data class Equipment(
    @DocumentId val id: String = "",
    val name: String = "",
    val category: String = "Other",
    val description: String = "",
    val hourlyRate: Double = 0.0,
    val imageUrls: List<String> = emptyList(),
    val ownerId: String = "",
    val ownerName: String = "",
    val ownerPhone: String = "",
    val location: String = "",
    val isAvailable: Boolean = true,
    val createdAt: Long = 0L
)

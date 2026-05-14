package com.nammayantra.app.data.model

import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId val id: String = "",
    val name: String = "",
    val role: String = "",
    val phone: String = ""
)

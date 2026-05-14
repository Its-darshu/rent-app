package com.nammayantra.app.data.model

import com.google.firebase.firestore.DocumentId

data class RentalRequest(
    @DocumentId val id: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val date: String = "",
    val durationText: String = "",
    val totalPrice: Double = 0.0,
    val status: String = "PENDING",
    val specialInstructions: String = ""
)

enum class RequestStatus { PENDING, ACCEPTED, DECLINED }

val RentalRequest.requestStatus: RequestStatus
    get() = try { RequestStatus.valueOf(status) } catch (_: Exception) { RequestStatus.PENDING }

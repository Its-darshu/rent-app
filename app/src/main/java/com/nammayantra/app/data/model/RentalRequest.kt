package com.nammayantra.app.data.model

data class RentalRequest(
    val id: String,
    val equipmentName: String,
    val farmerName: String,
    val ownerName: String,
    val date: String,
    val durationText: String,
    val totalPrice: Double,
    var status: RequestStatus
)

enum class RequestStatus {
    PENDING, ACCEPTED, DECLINED
}

// Simulated mock data for MVP
val mockRequests = mutableListOf(
    RentalRequest("REQ1", "Mahindra 275 DI TU", "Raju (Farmer)", "Ramesh (Owner)", "Oct 25, 2026", "4 hours", 2000.0, RequestStatus.PENDING),
    RentalRequest("REQ2", "Kubota W70", "Raju (Farmer)", "Mahesh (Owner)", "Oct 26, 2026", "1 days", 8000.0, RequestStatus.ACCEPTED),
    RentalRequest("REQ3", "Aspee Knapsack", "Raju (Farmer)", "Dinesh (Owner)", "Oct 27, 2026", "2 hours", 200.0, RequestStatus.DECLINED)
)
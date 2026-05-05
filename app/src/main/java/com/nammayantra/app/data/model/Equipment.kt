package com.nammayantra.app.data.model

data class Equipment(
    val id: String,
    val name: String,
    val type: String, // Tractor, Harvester, Sprayer
    val hourlyRate: Double,
    val dailyRate: Double,
    val distanceKm: Double,
    val isAvailable: Boolean,
    val healthCondition: String, // e.g., "Good", "Excellent"
    val ownerName: String
)

val mockEquipments = listOf(
    Equipment("1", "Mahindra 275 DI TU", "Tractor", 500.0, 3500.0, 2.5, true, "Excellent", "Ramesh"),
    Equipment("2", "John Deere 5050 D", "Tractor", 600.0, 4000.0, 5.2, false, "Good", "Suresh"),
    Equipment("3", "Kubota W70", "Harvester", 1200.0, 8000.0, 12.0, true, "Good", "Mahesh"),
    Equipment("4", "Aspee Knapsack", "Sprayer", 100.0, 500.0, 1.2, true, "Fair", "Dinesh")
)
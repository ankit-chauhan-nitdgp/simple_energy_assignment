package com.simple.energy.data.remote

data class VehicleDTO(
    val id: String,
    val name: String,
    val model: String,
    val batteryPercentage: Int,
    val range: Int,
    val isOnline: Boolean,
    val currentSpeed: Double,
    val odometer: Double,
    val connectivityStatus: String,
    val lastUpdated: String
)
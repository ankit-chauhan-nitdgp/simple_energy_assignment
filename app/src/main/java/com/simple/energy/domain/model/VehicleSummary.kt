package com.simple.energy.domain.model

data class VehicleSummary(
    val id: String,
    val name: String,
    val model: String,
    val batteryPercentage: Int,
    val range: Int,
    val isOnline: Boolean
)
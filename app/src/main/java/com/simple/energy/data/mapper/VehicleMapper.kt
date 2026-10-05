package com.simple.energy.data.mapper

import com.simple.energy.data.remote.VehicleDTO
import com.simple.energy.domain.model.VehicleDetails
import com.simple.energy.domain.model.VehicleSummary

fun VehicleDTO.toSummary(): VehicleSummary {
    return VehicleSummary(
        id = id,
        name = name,
        model = model,
        batteryPercentage = batteryPercentage,
        range = range,
        isOnline = isOnline
    )
}

fun VehicleDTO.toDetails(): VehicleDetails {
    return VehicleDetails(
        id = id,
        name = name,
        model = model,
        batteryPercentage = batteryPercentage,
        range = range,
        isOnline = isOnline,
        currentSpeed = currentSpeed,
        odometer = odometer,
        connectivityStatus = connectivityStatus,
        lastUpdated = lastUpdated
    )
}
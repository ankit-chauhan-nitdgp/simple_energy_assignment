package com.simple.energy.domain.repository

import com.simple.energy.domain.model.VehicleDetails
import com.simple.energy.domain.model.VehicleSummary
import com.simple.energy.core.ApiResult

interface VehicleRepository {

    suspend fun getVehicles(): ApiResult<List<VehicleSummary>>

    suspend fun getVehicleDetails(
        vehicleId: String
    ): ApiResult<VehicleDetails>
}
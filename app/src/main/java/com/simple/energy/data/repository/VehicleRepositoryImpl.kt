package com.simple.energy.data.repository

import com.simple.energy.core.network.safeApiCall
import com.simple.energy.data.mapper.toSummary
import com.simple.energy.data.remote.VehicleApi
import com.simple.energy.domain.model.VehicleDetails
import com.simple.energy.domain.model.VehicleSummary
import com.simple.energy.domain.repository.VehicleRepository
import jakarta.inject.Inject
import com.simple.energy.core.ApiResult
import com.simple.energy.data.mapper.toDetails

class VehicleRepositoryImpl @Inject constructor(
    private val api: VehicleApi
) : VehicleRepository {

    override suspend fun getVehicles(): ApiResult<List<VehicleSummary>> {
        return safeApiCall {
            api.getVehicles()
                .map { it.toSummary() }
        }
    }

    override suspend fun getVehicleDetails(
        vehicleId: String
    ): ApiResult<VehicleDetails> {
        return safeApiCall {
            api.getVehicleDetails(vehicleId)
                .toDetails()
        }
    }
}
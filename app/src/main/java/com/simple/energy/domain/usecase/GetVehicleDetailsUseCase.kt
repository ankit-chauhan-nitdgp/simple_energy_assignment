package com.simple.energy.domain.usecase

import com.simple.energy.core.ApiResult
import com.simple.energy.domain.model.VehicleDetails
import com.simple.energy.domain.repository.VehicleRepository
import jakarta.inject.Inject

class GetVehicleDetailsUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(
        vehicleId: String
    ): ApiResult<VehicleDetails> {
        return repository.getVehicleDetails(vehicleId)
    }
}
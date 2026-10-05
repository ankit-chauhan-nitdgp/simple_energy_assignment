package com.simple.energy.domain.usecase

import com.simple.energy.core.ApiResult
import com.simple.energy.domain.model.VehicleSummary
import com.simple.energy.domain.repository.VehicleRepository
import jakarta.inject.Inject

class GetVehicleUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(): ApiResult<List<VehicleSummary>> {
        return repository.getVehicles()
    }
}
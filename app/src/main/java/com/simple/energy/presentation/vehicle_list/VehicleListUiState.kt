package com.simple.energy.presentation.vehicle_list

import com.simple.energy.domain.model.VehicleSummary

data class VehicleListUiState(
    val isLoading: Boolean = false,
    val vehicles: List<VehicleSummary> = emptyList(),
    val errorMessage: String? = null
)
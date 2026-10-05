package com.simple.energy.presentation.vehicle_details

import com.simple.energy.domain.model.VehicleDetails

data class VehicleDetailsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val vehicle: VehicleDetails? = null,
    val errorMessage: String? = null
)
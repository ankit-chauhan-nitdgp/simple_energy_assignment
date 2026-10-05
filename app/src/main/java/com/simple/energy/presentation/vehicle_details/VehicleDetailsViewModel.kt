package com.simple.energy.presentation.vehicle_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.energy.core.ApiResult
import com.simple.energy.domain.usecase.GetVehicleDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class VehicleDetailsViewModel @Inject constructor(
    private val getVehicleDetailsUseCase: GetVehicleDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val vehicleId: String =
        checkNotNull(savedStateHandle["vehicleId"])

    private val _uiState = MutableStateFlow(VehicleDetailsUiState())
    val uiState: StateFlow<VehicleDetailsUiState> = _uiState.asStateFlow()

    init {
        loadVehicleDetails()
    }

    fun refresh() {
        loadVehicleDetails(isRefresh = true)
    }

    private fun loadVehicleDetails(isRefresh: Boolean = false) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                errorMessage = null
            )

            when (val result = getVehicleDetailsUseCase(vehicleId)) {

                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        vehicle = result.data,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}
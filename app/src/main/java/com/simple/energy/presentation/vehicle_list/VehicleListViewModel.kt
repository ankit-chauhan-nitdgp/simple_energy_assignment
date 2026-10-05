package com.simple.energy.presentation.vehicle_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.energy.core.ApiResult
import com.simple.energy.domain.usecase.GetVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class VehicleListViewModel @Inject constructor(
    private val getVehicleUseCase: GetVehicleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleListUiState())
    val uiState: StateFlow<VehicleListUiState> = _uiState.asStateFlow()

    init {
        loadVehicles()
    }

    fun retry() {
        loadVehicles()
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = getVehicleUseCase()) {

                is ApiResult.Success -> {
                    _uiState.value = VehicleListUiState(
                        isLoading = false,
                        vehicles = result.data
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}
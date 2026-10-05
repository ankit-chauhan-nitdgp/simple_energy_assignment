package com.simple.energy.presentation.vehicle_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simple.energy.presentation.components.EmptyContent
import com.simple.energy.presentation.components.ErrorContent
import com.simple.energy.presentation.components.LoadingContent
import com.simple.energy.presentation.components.VehicleList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleListScreen(
    onVehicleClick: (String) -> Unit,
    viewModel: VehicleListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Vehicles")
                }
            )
        }
    ) { paddingValues ->

        when {
            uiState.isLoading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            uiState.errorMessage != null && uiState.vehicles.isEmpty() -> {
                ErrorContent(
                    message = uiState.errorMessage,
                    onRetry = viewModel::retry,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            uiState.vehicles.isEmpty() -> {
                EmptyContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            else -> {
                VehicleList(
                    vehicles = uiState.vehicles,
                    onVehicleClick = onVehicleClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}
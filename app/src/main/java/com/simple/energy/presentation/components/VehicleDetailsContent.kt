package com.simple.energy.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simple.energy.domain.model.VehicleDetails

@Composable
fun VehicleDetailsContent(
    vehicle: VehicleDetails,
    isRefreshing: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        VehicleHeader(
            vehicle = vehicle
        )

        VehicleInfoRow(
            label = "Battery",
            value = "${vehicle.batteryPercentage}%"
        )

        VehicleInfoRow(
            label = "Estimated Range",
            value = "${vehicle.range} km"
        )

        VehicleInfoRow(
            label = "Current Speed",
            value = "${vehicle.currentSpeed} km/h"
        )

        VehicleInfoRow(
            label = "Odometer",
            value = "${vehicle.odometer} km"
        )

        VehicleInfoRow(
            label = "Connectivity",
            value = vehicle.connectivityStatus
        )

        VehicleInfoRow(
            label = "Last Updated",
            value = vehicle.lastUpdated
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retry")
            }
        }
    }
}
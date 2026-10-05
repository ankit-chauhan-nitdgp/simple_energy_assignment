package com.simple.energy.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simple.energy.domain.model.VehicleDetails

@Composable
fun VehicleHeader(
    vehicle: VehicleDetails
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = vehicle.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = vehicle.model,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = if (vehicle.isOnline) "Online" else "Offline",
            color = if (vehicle.isOnline) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            fontWeight = FontWeight.Medium
        )
    }
}
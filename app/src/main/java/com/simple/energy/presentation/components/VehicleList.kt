package com.simple.energy.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simple.energy.domain.model.VehicleSummary

@Composable
fun VehicleList(
    vehicles: List<VehicleSummary>,
    onVehicleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = vehicles,
            key = { it.id }
        ) { vehicle ->

            VehicleCard(
                vehicle = vehicle,
                onClick = {
                    onVehicleClick(vehicle.id)
                }
            )
        }
    }
}
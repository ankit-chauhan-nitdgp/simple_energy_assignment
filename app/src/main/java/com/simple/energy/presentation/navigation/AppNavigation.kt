package com.simple.energy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simple.energy.presentation.vehicle_details.VehicleDetailsScreen
import com.simple.energy.presentation.vehicle_list.VehicleListScreen

private object Routes {
    const val VEHICLE_LIST = "vehicle_list"
    const val VEHICLE_DETAILS = "vehicle_details/{vehicleId}"

    fun vehicleDetails(vehicleId: String): String {
        return "vehicle_details/$vehicleId"
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.VEHICLE_LIST
    ) {

        composable(
            route = Routes.VEHICLE_LIST
        ) {
            VehicleListScreen(
                onVehicleClick = { vehicleId ->
                    navController.navigate(
                        Routes.vehicleDetails(vehicleId)
                    )
                }
            )
        }

        composable(
            route = Routes.VEHICLE_DETAILS,
            arguments = listOf(
                navArgument("vehicleId") {
                    type = NavType.StringType
                }
            )
        ) {
            VehicleDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
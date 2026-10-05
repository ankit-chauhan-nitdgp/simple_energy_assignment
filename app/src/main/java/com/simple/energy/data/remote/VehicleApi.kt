package com.simple.energy.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleApi {

    @GET("api/vehicles")
    suspend fun getVehicles(): List<VehicleDTO>

    @GET("api/vehicles/{id}")
    suspend fun getVehicleDetails(
        @Path("id") vehicleId: String
    ): VehicleDTO

}
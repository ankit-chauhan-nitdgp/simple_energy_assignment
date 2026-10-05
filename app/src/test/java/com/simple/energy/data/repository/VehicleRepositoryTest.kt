package com.simple.energy.data.repository

import com.simple.energy.core.ApiResult
import com.simple.energy.data.remote.VehicleApi
import com.simple.energy.data.remote.VehicleDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VehicleRepositoryTest {

    private lateinit var api: VehicleApi
    private lateinit var repository: VehicleRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        repository = VehicleRepositoryImpl(api)
    }

    @Test
    fun `getVehicles returns mapped vehicle summaries on success`() = runTest {

        val dto = VehicleDTO(
            id = "EV001",
            name = "Astra",
            model = "Gen 2",
            batteryPercentage = 92,
            range = 145,
            isOnline = true,
            currentSpeed = 42.5,
            odometer = 4832.7,
            connectivityStatus = "CONNECTED",
            lastUpdated = "2026-10-05T11:45:00Z"
        )

        coEvery {
            api.getVehicles()
        } returns listOf(dto)

        val result = repository.getVehicles()

        assertTrue(result is ApiResult.Success)

        val vehicles = (result as ApiResult.Success).data

        assertEquals(1, vehicles.size)

        val vehicle = vehicles.first()

        assertEquals("EV001", vehicle.id)
        assertEquals("Astra", vehicle.name)
        assertEquals("Gen 2", vehicle.model)
        assertEquals(92, vehicle.batteryPercentage)
        assertEquals(145, vehicle.range)
        assertTrue(vehicle.isOnline)
    }

    @Test
    fun `getVehicleDetails returns mapped vehicle details on success`() = runTest {

        val dto = VehicleDTO(
            id = "EV001",
            name = "Astra",
            model = "Gen 2",
            batteryPercentage = 92,
            range = 145,
            isOnline = true,
            currentSpeed = 42.5,
            odometer = 4832.7,
            connectivityStatus = "CONNECTED",
            lastUpdated = "2026-10-05T11:45:00Z"
        )

        coEvery {
            api.getVehicleDetails("EV001")
        } returns dto

        val result = repository.getVehicleDetails("EV001")

        assertTrue(result is ApiResult.Success)

        val vehicle = (result as ApiResult.Success).data

        assertEquals("EV001", vehicle.id)
        assertEquals("Astra", vehicle.name)
        assertEquals(92, vehicle.batteryPercentage)
        assertEquals(145, vehicle.range)
        assertEquals(42.5, vehicle.currentSpeed, 0.0)
        assertEquals(4832.7, vehicle.odometer, 0.0)
        assertEquals("CONNECTED", vehicle.connectivityStatus)
    }

    @Test
    fun `getVehicles returns error when api throws exception`() = runTest {

        coEvery {
            api.getVehicles()
        } throws java.io.IOException("Network unavailable")

        val result = repository.getVehicles()

        assertTrue(result is ApiResult.Error)

        val error = result as ApiResult.Error

        assertEquals(
            "Network error. Please check your connection.",
            error.message
        )
    }
}
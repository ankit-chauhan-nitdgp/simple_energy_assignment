package com.simple.energy.presentation.vehicle_details

import androidx.lifecycle.SavedStateHandle
import com.simple.energy.core.ApiResult
import com.simple.energy.domain.model.VehicleDetails
import com.simple.energy.domain.repository.VehicleRepository
import com.simple.energy.domain.usecase.GetVehicleDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleDetailsViewModelTest {

    private lateinit var repository: VehicleRepository
    private lateinit var useCase: GetVehicleDetailsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        useCase = GetVehicleDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load vehicle details returns vehicle on success`() = runTest {

        val vehicle = VehicleDetails(
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
            repository.getVehicleDetails("EV001")
        } returns ApiResult.Success(vehicle)

        val savedStateHandle = SavedStateHandle(
            mapOf("vehicleId" to "EV001")
        )

        val viewModel = VehicleDetailsViewModel(
            getVehicleDetailsUseCase = useCase,
            savedStateHandle = savedStateHandle
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.isRefreshing)
        assertEquals(vehicle, state.vehicle)
        assertTrue(state.errorMessage == null)
    }

    @Test
    fun `refresh updates vehicle details`() = runTest {

        val firstVehicle = VehicleDetails(
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

        val refreshedVehicle = firstVehicle.copy(
            batteryPercentage = 88,
            currentSpeed = 55.0,
            lastUpdated = "2026-10-05T12:00:00Z"
        )

        coEvery {
            repository.getVehicleDetails("EV001")
        } returnsMany listOf(
            ApiResult.Success(firstVehicle),
            ApiResult.Success(refreshedVehicle)
        )

        val savedStateHandle = SavedStateHandle(
            mapOf("vehicleId" to "EV001")
        )

        val viewModel = VehicleDetailsViewModel(
            getVehicleDetailsUseCase = useCase,
            savedStateHandle = savedStateHandle
        )

        advanceUntilIdle()

        assertEquals(92, viewModel.uiState.value.vehicle?.batteryPercentage)

        viewModel.refresh()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isRefreshing)
        assertEquals(88, state.vehicle?.batteryPercentage)
        assertEquals(55.0, state.vehicle?.currentSpeed)
        assertEquals(
            "2026-10-05T12:00:00Z",
            state.vehicle?.lastUpdated
        )
    }

    @Test
    fun `load vehicle details returns error on failure`() = runTest {

        coEvery {
            repository.getVehicleDetails("EV001")
        } returns ApiResult.Error(
            message = "Network error. Please check your connection."
        )

        val savedStateHandle = SavedStateHandle(
            mapOf("vehicleId" to "EV001")
        )

        val viewModel = VehicleDetailsViewModel(
            getVehicleDetailsUseCase = useCase,
            savedStateHandle = savedStateHandle
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.isRefreshing)
        assertTrue(state.vehicle == null)

        assertEquals(
            "Network error. Please check your connection.",
            state.errorMessage
        )
    }
}
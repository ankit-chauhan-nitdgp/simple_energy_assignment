package com.simple.energy.presentation.vehicle_list

import com.simple.energy.core.ApiResult
import com.simple.energy.domain.model.VehicleSummary
import com.simple.energy.domain.repository.VehicleRepository
import com.simple.energy.domain.usecase.GetVehicleUseCase
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
class VehicleListViewModelTest {

    private lateinit var repository: VehicleRepository
    private lateinit var useCase: GetVehicleUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        useCase = GetVehicleUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load vehicles updates state with vehicles on success`() = runTest {

        val vehicles = listOf(
            VehicleSummary(
                id = "EV001",
                name = "Astra",
                model = "Gen 2",
                batteryPercentage = 92,
                range = 145,
                isOnline = true
            ),
            VehicleSummary(
                id = "EV002",
                name = "Nova",
                model = "Gen 1",
                batteryPercentage = 68,
                range = 110,
                isOnline = false
            )
        )

        coEvery {
            repository.getVehicles()
        } returns ApiResult.Success(vehicles)

        val viewModel = VehicleListViewModel(useCase)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(2, state.vehicles.size)
        assertEquals("EV001", state.vehicles[0].id)
        assertEquals("Astra", state.vehicles[0].name)
        assertEquals(92, state.vehicles[0].batteryPercentage)
        assertTrue(state.errorMessage == null)
    }

    @Test
    fun `load vehicles updates state with error on failure`() = runTest {

        coEvery {
            repository.getVehicles()
        } returns ApiResult.Error(
            message = "Network error. Please check your connection."
        )

        val viewModel = VehicleListViewModel(useCase)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.vehicles.isEmpty())

        assertEquals(
            "Network error. Please check your connection.",
            state.errorMessage
        )
    }

    @Test
    fun `retry loads vehicles again`() = runTest {

        coEvery {
            repository.getVehicles()
        } returnsMany listOf(
            ApiResult.Error("Network error"),
            ApiResult.Success(
                listOf(
                    VehicleSummary(
                        id = "EV001",
                        name = "Astra",
                        model = "Gen 2",
                        batteryPercentage = 92,
                        range = 145,
                        isOnline = true
                    )
                )
            )
        )

        val viewModel = VehicleListViewModel(useCase)

        advanceUntilIdle()

        assertEquals("Network error", viewModel.uiState.value.errorMessage)

        viewModel.retry()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.vehicles.size)
        assertEquals("Astra", state.vehicles.first().name)
        assertTrue(state.errorMessage == null)
    }
}
package com.simple.energy.di

import com.simple.energy.data.repository.VehicleRepositoryImpl
import com.simple.energy.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(
        implementation: VehicleRepositoryImpl
    ): VehicleRepository
}
package com.example.measurementbook.network.modules

import com.example.measurementbook.network.impl.AccountServiceImpl
import com.example.measurementbook.network.impl.LogServiceImpl
import com.example.measurementbook.network.impl.StoragesServiceImpl
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.Storage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindAccountService(
        accountServiceImpl: AccountServiceImpl
    ): AccountService

    @Binds
    abstract fun bindLogService(
        logServiceImpl: LogServiceImpl
    ): LogService

    @Binds
    abstract fun bindStorageService(
        storageServiceImpl: StoragesServiceImpl
    ): Storage
}
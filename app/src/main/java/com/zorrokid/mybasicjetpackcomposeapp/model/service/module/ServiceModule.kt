package com.zorrokid.mybasicjetpackcomposeapp.model.service.module

import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.BarcodeScanService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.ReleaseAreaService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.StorageService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.AccountServiceImpl
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.BarcodeScanServiceImpl
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.LogServiceImpl
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.ReleaseAreaServiceImpl
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module provides app services for dependency injection with Hilt.
 *
 * Notes for myself:
 * A Hilt module is a class that is annotated with @Module.
 * It informs Hilt how to provide instances of certain types.
 * You must annotate Hilt modules with @InstallIn to tell Hilt which
 * Android class each module will be used or installed in.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    // Inject interface with @Binds
    // The @Binds annotation tells Hilt which implementation to use when it needs to provide an instance of an interface.
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService
    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
    @Binds abstract fun provideBarcodeScanService(impl: BarcodeScanServiceImpl): BarcodeScanService
    @Binds abstract fun provideReleaseAreaService(impl: ReleaseAreaServiceImpl): ReleaseAreaService
}
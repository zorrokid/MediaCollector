package com.zorrokid.mybasicjetpackcomposeapp.model.service.module

import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.AccountServiceImpl
import com.zorrokid.mybasicjetpackcomposeapp.model.service.impl.LogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// A Hilt module is a class that is annotated with @Module.
// Like a Dagger module, it informs Hilt how to provide instances of certain types.
// Unlike Dagger modules, you must annotate Hilt modules with @InstallIn to tell Hilt which
// Android class each module will be used or installed in.
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    // Inject interface with @Binds
    // The @Binds annotation tells Hilt which implementation to use when it needs to provide an instance of an interface.
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService
}
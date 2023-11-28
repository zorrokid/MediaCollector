package com.zorrokid.mybasicjetpackcomposeapp.model.service.module

import android.app.Application
import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides the barcode scanner with context and options for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object BarcodeScanModule {

    @Provides
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    fun provideBarCodeOptions() : GmsBarcodeScannerOptions {
        return GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_EAN_13)
            .enableAutoZoom()
            .build()
    }

    @Provides
    fun provideBarCodeScanner(context: Context, options: GmsBarcodeScannerOptions) : GmsBarcodeScanner{
        return GmsBarcodeScanning.getClient(context, options)
    }
}
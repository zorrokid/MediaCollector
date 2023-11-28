package com.zorrokid.mybasicjetpackcomposeapp.model.service.impl

import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.zorrokid.mybasicjetpackcomposeapp.model.service.BarcodeScanService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarcodeScanServiceImpl @Inject constructor(
    private val scanner: GmsBarcodeScanner
) : BarcodeScanService {

    // Flow: An asynchronous data stream that sequentially emits values and completes normally or with an exception.
    override fun startScanning(): Flow<String?> {

        // callbackFlow creates an instance of a cold Flow with elements that are sent to a
        // SendChannel provided to the builder's block of code via ProducerScope.
        // It allows elements to be produced by code that is running in a different context or concurrently.
        // The resulting flow is cold, which means that block is called every time a
        // terminal operator is applied to the resulting flow.
        // https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/callback-flow.html
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch {
                        send(it.rawValue)
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }
            awaitClose {  }
        }
    }
}
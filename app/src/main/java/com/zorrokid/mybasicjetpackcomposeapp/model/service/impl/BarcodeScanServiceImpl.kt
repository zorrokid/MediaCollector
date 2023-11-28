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
    override fun startScanning(): Flow<String?> {
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
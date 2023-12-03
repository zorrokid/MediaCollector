package com.zorrokid.mediacollector.model.service

import kotlinx.coroutines.flow.Flow

interface BarcodeScanService {
    fun startScanning(): Flow<String?>
}
package com.zorrokid.mybasicjetpackcomposeapp.model.service

import kotlinx.coroutines.flow.Flow

interface BarcodeScanService {
    fun startScanning(): Flow<String?>
}
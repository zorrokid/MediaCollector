package com.zorrokid.mediacollector.model.service.impl

import android.util.Log
import com.zorrokid.mediacollector.model.service.LogService
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService{
    override fun logNonFatalCrash(throwable: Throwable) {
        Log.e("LogServiceImpl", throwable.toString())
    }
}
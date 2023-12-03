package com.zorrokid.mediacollector.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
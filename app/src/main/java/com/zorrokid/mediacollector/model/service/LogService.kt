package com.zorrokid.mybasicjetpackcomposeapp.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
package com.zorrokid.mybasicjetpackcomposeapp.model.service

import com.zorrokid.mybasicjetpackcomposeapp.model.ReleaseArea
import kotlinx.coroutines.flow.Flow

interface ReleaseAreaService {
    val releaseAreas: Flow<List<ReleaseArea>>
}
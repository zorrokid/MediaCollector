package com.zorrokid.mediacollector.model.service

import com.zorrokid.mediacollector.model.ReleaseArea
import kotlinx.coroutines.flow.Flow

interface ReleaseAreaService {
    val releaseAreas: Flow<List<ReleaseArea>>
}
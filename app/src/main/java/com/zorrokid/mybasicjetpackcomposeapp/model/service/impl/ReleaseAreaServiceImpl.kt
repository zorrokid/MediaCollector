package com.zorrokid.mybasicjetpackcomposeapp.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.zorrokid.mybasicjetpackcomposeapp.model.ReleaseArea
import com.zorrokid.mybasicjetpackcomposeapp.model.service.ReleaseAreaService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReleaseAreaServiceImpl @Inject constructor(private val firestore: FirebaseFirestore):
    ReleaseAreaService {
    override val releaseAreas: Flow<List<ReleaseArea>>
        get() = firestore.collection(RELEASE_AREA_COLLECTION).dataObjects()

    companion object {
        private const val RELEASE_AREA_COLLECTION = "releaseAreas"
    }
}
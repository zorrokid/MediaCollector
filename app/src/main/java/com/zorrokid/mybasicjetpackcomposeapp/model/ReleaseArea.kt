package com.zorrokid.mybasicjetpackcomposeapp.model

import com.google.firebase.firestore.DocumentId

data class ReleaseArea(
    @DocumentId val id: String = "",
    val name: String = "",
    val countryCodes: List<String> = emptyList(),
)


package com.zorrokid.mediacollector.model

import com.google.firebase.firestore.DocumentId

data class ReleaseArea(
    @DocumentId override val id: String = "",
    override  val name: String = "",
    val countryCodes: List<String> = emptyList(),
) : IdAndNameObject


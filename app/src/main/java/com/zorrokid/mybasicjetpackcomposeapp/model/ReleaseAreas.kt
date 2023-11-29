package com.zorrokid.mybasicjetpackcomposeapp.model

object ReleaseAreas {
    // ISO 3166-1 alpha-2 codes used
    val releaseAreas: List<ReleaseArea> = listOf(
        ReleaseArea(setOf("FI"), "Finland"),
        ReleaseArea(setOf("SE"), "Sweden"),
        ReleaseArea(setOf("NO"), "Norway"),
        ReleaseArea(setOf("DK"), "Denmark"),
        ReleaseArea(setOf("FI", "SE", "DK", "NO"), "Nordic Countries"),
    )
}
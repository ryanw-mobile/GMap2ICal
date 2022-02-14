package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
    val accuracyMetres: Int? = null,
    val address: String? = null,
    val calibratedProbability: Double? = null,
    val latitudeE7: Int,
    val locationConfidence: Double? = null,
    val longitudeE7: Int,
    val name: String? = null,
    val placeId: String? = null,
    val semanticType: String? = null,
)
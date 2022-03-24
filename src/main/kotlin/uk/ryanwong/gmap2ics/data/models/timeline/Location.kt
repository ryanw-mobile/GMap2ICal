package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
    val accuracyMetres: Int? = null, // not exists for StartLocation and EndLocation
    val address: String? = null,
    val calibratedProbability: Double? = null,
    val latitudeE7: Int,
    val locationConfidence: Double? = null,
    val longitudeE7: Int,
    val name: String? = null,
    val placeId: String? = null,
    val semanticType: String? = null,
)
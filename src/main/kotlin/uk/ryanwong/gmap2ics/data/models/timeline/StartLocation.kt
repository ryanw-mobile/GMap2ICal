package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class StartLocation(
    val address: String? = null,
    val calibratedProbability: Double? = null,
    val latitudeE7: Int? = null,
    val locationConfidence: Double? = null,
    val longitudeE7: Int? = null,
    val name: String? = null,
    val placeId: String? = null,
    val semanticType: String? = null,
)
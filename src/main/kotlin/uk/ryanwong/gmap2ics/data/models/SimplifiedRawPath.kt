package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SimplifiedRawPath(
    val points: List<Point>? = null,
    val source: String? = null,
    val distanceMeters: Double? = null,
)
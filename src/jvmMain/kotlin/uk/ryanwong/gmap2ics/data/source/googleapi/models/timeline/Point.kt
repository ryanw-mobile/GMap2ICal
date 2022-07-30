package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Point(
    val accuracyMeters: Int? = null,
    val latE7: Int? = null,
    val lngE7: Int? = null,
    val timestamp: String? = null
)
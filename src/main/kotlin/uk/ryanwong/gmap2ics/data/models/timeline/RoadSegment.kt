package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RoadSegment(
    val duration: String? = null,
    val placeId: String? = null,
)
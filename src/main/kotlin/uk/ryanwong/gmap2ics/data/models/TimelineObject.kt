package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TimelineObject(
    val activitySegment: ActivitySegment? = null,
    val placeVisit: PlaceVisit? = null
)
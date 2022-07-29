package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OriginalCandidates(
    val placeVisitSegment: PlaceVisitSegment? = null
)
package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EditActionMetadata(
    val editHistory: EditHistory? = null,
    val originalCandidates: OriginalCandidates? = null,
    val placeVisitSegment: PlaceVisitSegment? = null,
    val activitySegment: ActivitySegment? = null
)
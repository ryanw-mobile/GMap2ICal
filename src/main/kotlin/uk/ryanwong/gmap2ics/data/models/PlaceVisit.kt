package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlaceVisit(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val checkin: Checkin? = null,
    val duration: Duration? = null,
    val editActionMetadata: EditActionMetadata? = null,
    val editConfirmationStatus: String? = null,
    val lastEditedTimestamp: String? = null,
    val location: Location,
    val locationAssertionType: String? = null,
    val locationConfidence: Int? = null,
    val otherCandidateLocations: List<OtherCandidateLocation>? = null,
    val placeConfidence: String? = null,
    val placeVisitImportance: String? = null,
    val placeVisitType: String? = null,
    val simplifiedRawPath: SimplifiedRawPath? = null,
    val visitConfidence: Int? = null,
    val childVisits: List<ChildVisit>? = null,
)
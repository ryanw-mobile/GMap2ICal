package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChildVisit(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val duration: Duration? = null,
    val editConfirmationStatus: String? = null,
    val lastEditedTimestamp: String? = null,
    val location: Location,
    val locationAssertionType: String? = null,
    val locationConfidence: Int? = null,
    val placeConfidence: String? = null,
    val placeVisitImportance: String? = null,
    val placeVisitType: String? = null,
    val simplifiedRawPath: SimplifiedRawPath? = null,
    val visitConfidence: Int? = null,
    val placeVisitLevel: Int? = null
)
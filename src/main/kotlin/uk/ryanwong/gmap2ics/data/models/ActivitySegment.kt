package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ActivitySegment(
    val activities: List<Activity>? = null,
    val activityType: String? = null,
    val confidence: String? = null,
    val distance: Int? = null,
    val duration: Duration? = null,
    val editActionMetadata: EditActionMetadata? = null,
    val endLocation: EndLocation? = null,
    val parkingEvent: ParkingEvent? = null,
    val simplifiedRawPath: SimplifiedRawPath? = null,
    val startLocation: StartLocation? = null,
    val waypointPath: WaypointPath? = null,
    val editConfirmationStatus: String? = null,
    val lastEditedTimestamp: String? = null,
    val activityConfidence: Int? = null
)
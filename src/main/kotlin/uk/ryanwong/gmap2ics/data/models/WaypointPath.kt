package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class WaypointPath(
    val confidence: Double? = null,
    val distanceMeters: Double? = null,
    val roadSegment: List<RoadSegment>? = null,
    val source: String? = null,
    val travelMode: String? = null,
    val waypoints: List<Waypoint>? = null
)
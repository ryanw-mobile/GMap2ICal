/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class WaypointPath(
    val confidence: Double? = null,
    val distanceMeters: Double? = null,
    val roadSegment: List<RoadSegment>? = null,
    val source: String? = null,
    val travelMode: String? = null,
)

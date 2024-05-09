/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class WaypointPathDto(
    val confidence: Double? = null,
    val distanceMeters: Double? = null,
    val roadSegmentDto: List<RoadSegmentDto>? = null,
    val source: String? = null,
    val travelMode: String? = null,
)

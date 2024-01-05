/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

data class WaypointPath(
    val distanceMeters: Double,
    val roadSegmentPlaceIds: List<String>,
)

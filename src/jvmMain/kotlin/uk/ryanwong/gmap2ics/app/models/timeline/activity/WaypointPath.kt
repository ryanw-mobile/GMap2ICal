/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

data class WaypointPath(
    val distanceMeters: Double,
    val roadSegmentPlaceIds: List<String>,
)

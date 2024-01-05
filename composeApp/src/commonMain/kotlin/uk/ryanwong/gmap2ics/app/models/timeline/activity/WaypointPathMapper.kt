/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

fun uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath.toDomainModel(): WaypointPath {
    return WaypointPath(
        distanceMeters = distanceMeters ?: 0.0,
        roadSegmentPlaceIds = roadSegment?.mapNotNull { roadSegment ->
            roadSegment.placeId
        } ?: emptyList(),
    )
}

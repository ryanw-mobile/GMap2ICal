/*
 * Copyright (c) 2023. Ryan Wong (hello@ryanwong.co.uk)
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

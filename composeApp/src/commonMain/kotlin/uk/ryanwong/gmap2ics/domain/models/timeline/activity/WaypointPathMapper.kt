/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.activity

fun uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto.toDomainModel(): WaypointPath {
    return WaypointPath(
        distanceMeters = distanceMeters ?: 0.0,
        roadSegmentPlaceIds = roadSegmentDto?.mapNotNull { roadSegment ->
            roadSegment.placeId
        } ?: emptyList(),
    )
}

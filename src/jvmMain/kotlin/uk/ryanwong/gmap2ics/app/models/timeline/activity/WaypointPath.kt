/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

data class WaypointPath(
    val distanceMeters: Double,
    val roadSegmentPlaceIds: List<String>
) {
    companion object {
        fun from(waypointPathDataModel: uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath): WaypointPath {
            return WaypointPath(
                distanceMeters = waypointPathDataModel.distanceMeters ?: 0.0,
                roadSegmentPlaceIds = waypointPathDataModel.roadSegment?.mapNotNull { roadSegment ->
                    roadSegment.placeId
                } ?: emptyList()
            )
        }
    }
}

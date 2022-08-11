/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

data class WaypointPath(
    val distanceMeters: Double,
    val roadSegment: List<RoadSegment>,
//    val confidence: Double? = null,
//    val source: String? = null,
//    val travelMode: String? = null
) {
    companion object {
        fun from(waypointPathDataModel: uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.WaypointPath): WaypointPath {
            return WaypointPath(
                distanceMeters = waypointPathDataModel.distanceMeters ?: 0.0,
                roadSegment = waypointPathDataModel.roadSegment?.mapNotNull { roadSegment ->
                    if (roadSegment.duration == null || roadSegment.placeId == null) {
                        null
                    } else {
                        RoadSegment(
                            duration = roadSegment.duration,
                            placeId = roadSegment.placeId
                        )
                    }
                } ?: emptyList()
            )
        }
    }
}
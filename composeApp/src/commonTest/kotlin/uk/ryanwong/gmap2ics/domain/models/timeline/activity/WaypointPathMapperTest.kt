/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.activity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.data.models.timeline.RoadSegmentDto

internal class WaypointPathMapperTest : FreeSpec() {

    init {
        "toDomainModel()" - {
            "should correctly map Data Model to Domain Model" {
                val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
                    confidence = 98.7,
                    distanceMeters = 123.45,
                    roadSegmentDto = listOf(
                        RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                        RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                        RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
                    ),
                    source = "some-source",
                    travelMode = "some-travel-mode",
                )

                val waypointPath = waypointPathDtoDataModel.toDomainModel()

                waypointPath shouldBe WaypointPath(
                    distanceMeters = 123.45,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
                )
            }

            "should still correctly map Data Model to Domain Model when distanceMeters is null" {
                val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
                    confidence = 98.7,
                    distanceMeters = null,
                    roadSegmentDto = listOf(
                        RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                        RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                        RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
                    ),
                    source = "some-source",
                    travelMode = "some-travel-mode",
                )

                val waypointPath = waypointPathDtoDataModel.toDomainModel()

                waypointPath shouldBe WaypointPath(
                    distanceMeters = 0.0,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
                )
            }

            "should still correctly map Data Model to Domain Model when roadSegment is null" {
                val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
                    confidence = 98.7,
                    distanceMeters = 123.45,
                    roadSegmentDto = null,
                    source = "some-source",
                    travelMode = "some-travel-mode",
                )

                val waypointPath = waypointPathDtoDataModel.toDomainModel()

                waypointPath shouldBe WaypointPath(
                    distanceMeters = 123.45,
                    roadSegmentPlaceIds = emptyList(),
                )
            }

            "should still correctly map Data Model to Domain Model when roadSegment contains some invalid entries" {
                val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
                    confidence = 98.7,
                    distanceMeters = 123.45,
                    roadSegmentDto = listOf(
                        RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                        RoadSegmentDto(duration = null, placeId = null),
                        RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                        RoadSegmentDto(duration = null, placeId = null),
                        RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
                    ),
                    source = "some-source",
                    travelMode = "some-travel-mode",
                )

                val waypointPath = waypointPathDtoDataModel.toDomainModel()

                waypointPath shouldBe WaypointPath(
                    distanceMeters = 123.45,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
                )
            }
        }
    }
}

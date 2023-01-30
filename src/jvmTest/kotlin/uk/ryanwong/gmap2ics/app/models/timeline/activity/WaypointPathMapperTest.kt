/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.data.models.timeline.RoadSegment

internal class WaypointPathMapperTest : FreeSpec() {

    init {
        "toDomainModel()" - {
            "should correctly map Data Model to Domain Model" {
                // 🔴 Given
                val waypointPathDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath(
                    confidence = 98.7,
                    distanceMeters = 123.45,
                    roadSegment = listOf(
                        RoadSegment(duration = null, placeId = "some-place-id-1"),
                        RoadSegment(duration = null, placeId = "some-place-id-2"),
                        RoadSegment(duration = null, placeId = "some-place-id-3")
                    ),
                    source = "some-source",
                    travelMode = "some-travel-mode"
                )

                // 🟡 When
                val waypointPath = waypointPathDataModel.toDomainModel()

                // 🟢 Then
                waypointPath shouldBe WaypointPath(
                    distanceMeters = 123.45,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                )
            }

            "should still correctly map Data Model to Domain Model when distanceMeters is null" {
                // 🔴 Given
                val waypointPathDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath(
                    confidence = 98.7,
                    distanceMeters = null,
                    roadSegment = listOf(
                        RoadSegment(duration = null, placeId = "some-place-id-1"),
                        RoadSegment(duration = null, placeId = "some-place-id-2"),
                        RoadSegment(duration = null, placeId = "some-place-id-3")
                    ),
                    source = "some-source",
                    travelMode = "some-travel-mode"
                )

                // 🟡 When
                val waypointPath = waypointPathDataModel.toDomainModel()

                // 🟢 Then
                waypointPath shouldBe WaypointPath(
                    distanceMeters = 0.0,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                )
            }

            "should still correctly map Data Model to Domain Model when roadSegment is null" {
                // 🔴 Given
                val waypointPathDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath(
                    confidence = 98.7,
                    distanceMeters = 123.45,
                    roadSegment = null,
                    source = "some-source",
                    travelMode = "some-travel-mode"
                )

                // 🟡 When
                val waypointPath = waypointPathDataModel.toDomainModel()

                // 🟢 Then
                waypointPath shouldBe WaypointPath(
                    distanceMeters = 123.45,
                    roadSegmentPlaceIds = emptyList()
                )
            }

            "should still correctly map Data Model to Domain Model when roadSegment contains some invalid entries" {
                // 🔴 Given
                val waypointPathDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath(
                    confidence = 98.7,
                    distanceMeters = 123.45,
                    roadSegment = listOf(
                        RoadSegment(duration = null, placeId = "some-place-id-1"),
                        RoadSegment(duration = null, placeId = null),
                        RoadSegment(duration = null, placeId = "some-place-id-2"),
                        RoadSegment(duration = null, placeId = null),
                        RoadSegment(duration = null, placeId = "some-place-id-3")
                    ),
                    source = "some-source",
                    travelMode = "some-travel-mode"
                )

                // 🟡 When
                val waypointPath = waypointPathDataModel.toDomainModel()

                // 🟢 Then
                waypointPath shouldBe WaypointPath(
                    distanceMeters = 123.45,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                )
            }
        }
    }
}

/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.activity

import uk.ryanwong.gmap2ics.data.models.timeline.RoadSegmentDto
import uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WaypointPathMapperTest {

    @Test
    fun `returns correct WaypointPath when all fields are valid`() {
        val waypointPathDtoDataModel = WaypointPathDto(
            confidence = 98.7,
            distanceMeters = 123.45,
            roadSegment = listOf(
                RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
            ),
            source = "some-source",
            travelMode = "some-travel-mode",
        )
        val expectedWaypointPath = WaypointPath(
            distanceMeters = 123.45,
            roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
        )

        val waypointPath = waypointPathDtoDataModel.toDomainModel()
        assertEquals(expectedWaypointPath, waypointPath)
    }

    @Test
    fun `returns correct WaypointPath when distanceMeters is null`() {
        val waypointPathDtoDataModel = WaypointPathDto(
            confidence = 98.7,
            distanceMeters = null,
            roadSegment = listOf(
                RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
            ),
            source = "some-source",
            travelMode = "some-travel-mode",
        )
        val expectedWaypointPath = WaypointPath(
            distanceMeters = 0.0,
            roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
        )

        val waypointPath = waypointPathDtoDataModel.toDomainModel()
        assertEquals(expectedWaypointPath, waypointPath)
    }

    @Test
    fun `returns correct WaypointPath when roadSegment is null`() {
        val waypointPathDtoDataModel = WaypointPathDto(
            confidence = 98.7,
            distanceMeters = 123.45,
            roadSegment = null,
            source = "some-source",
            travelMode = "some-travel-mode",
        )
        val expectedWaypointPath = WaypointPath(
            distanceMeters = 123.45,
            roadSegmentPlaceIds = emptyList(),
        )

        val waypointPath = waypointPathDtoDataModel.toDomainModel()
        assertEquals(expectedWaypointPath, waypointPath)
    }

    @Test
    fun `returns correct WaypointPath when roadSegment contains null placeIds`() {
        val waypointPathDtoDataModel = WaypointPathDto(
            confidence = 98.7,
            distanceMeters = 123.45,
            roadSegment = listOf(
                RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                RoadSegmentDto(duration = null, placeId = null),
                RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                RoadSegmentDto(duration = null, placeId = null),
                RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
            ),
            source = "some-source",
            travelMode = "some-travel-mode",
        )
        val expectedWaypointPath = WaypointPath(
            distanceMeters = 123.45,
            roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
        )

        val waypointPath = waypointPathDtoDataModel.toDomainModel()
        assertEquals(expectedWaypointPath, waypointPath)
    }
}

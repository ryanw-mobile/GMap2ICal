/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.activity

import uk.ryanwong.gmap2ics.data.models.timeline.RoadSegmentDto
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WaypointPathMapperTest {

    @Test
    fun `toDomainModel() should correctly map Data Model to Domain Model`() {
        val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
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
    fun `toDomainModel() should still correctly map Data Model to Domain Model when distanceMeters is null`() {
        val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
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
    fun `toDomainModel() should still correctly map Data Model to Domain Model when roadSegment is null`() {
        val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
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
    fun `toDomainModel() should still correctly map Data Model to Domain Model when roadSegment contains some invalid entries`() {
        val waypointPathDtoDataModel = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto(
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

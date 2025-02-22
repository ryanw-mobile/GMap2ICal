/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.activity

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocationDto
import uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegmentDtoTestData.activitySegmentDto
import uk.ryanwong.gmap2ics.domain.models.ActivityType
import uk.ryanwong.gmap2ics.domain.models.ActivityType.IN_PASSENGER_VEHICLE
import uk.ryanwong.gmap2ics.domain.models.ActivityType.MOTORCYCLING
import uk.ryanwong.gmap2ics.domain.models.ActivityType.WALKING
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import us.dustinj.timezonemap.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ActivitySegmentMapperTest {

    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap

    // Standard domain model as the baseline
    // Tests only have to modify the necessary properties to highlight test focus
    private val activitySegmentDomainModel = ActivitySegment(
        activities = listOf(
            Activity(
                activityType = IN_PASSENGER_VEHICLE,
                rawActivityType = "IN_PASSENGER_VEHICLE",
            ),
            Activity(activityType = WALKING, rawActivityType = "WALKING"),
            Activity(activityType = MOTORCYCLING, rawActivityType = "MOTORCYCLING"),
        ),
        activityType = IN_PASSENGER_VEHICLE,
        rawActivityType = "IN_PASSENGER_VEHICLE",
        distance = 15032,
        durationEndTimestamp = RawTimestamp(timestamp = "2019-06-01T01:24:28Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(
            timestamp = "2019-06-01T01:04:01Z",
            timezoneId = "Asia/Tokyo",
        ),
        endLocation = Location(
            placeId = null,
            latitudeE7 = 344643393,
            longitudeE7 = 1324226167,
            name = null,
            address = null,
        ),
        startLocation = Location(
            placeId = null,
            latitudeE7 = 343970563,
            longitudeE7 = 1324677422,
            name = null,
            address = null,
        ),
        waypointPath = WaypointPath(
            distanceMeters = 15444.856340505617,
            roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3"),
        ),
        lastEditedTimestamp = "2019-06-01T01:24:28Z",
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
    )

    @Test
    fun `toDomainModel() should convert correctly from ActivitySegment Data Model to Domain Model`() {
        val activitySegmentDataModel = activitySegmentDto
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Asia/Tokyo"
        }
        val expectedDomainModel = activitySegmentDomainModel

        val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertEquals(expectedDomainModel, activitySegmentDomainModel)
    }

    @Test
    fun `toDomainModel() should still convert correctly from Data Model to Domain Model when rawActivityType is null`() {
        val activitySegmentDataModel = activitySegmentDto.copy(
            activityType = null,
        )
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Asia/Tokyo"
        }
        val expectedDomainModel = activitySegmentDomainModel.copy(
            activityType = ActivityType.UNKNOWN_ACTIVITY_TYPE,
            rawActivityType = null,
        )

        val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertEquals(expectedDomainModel, activitySegmentDomainModel)
    }

    @Test
    fun `toDomainModel() should still convert correctly from Data Model to Domain Model when activities is null`() {
        val activitySegmentDataModel = activitySegmentDto.copy(
            activities = null,
        )
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Asia/Tokyo"
        }
        val expectedDomainModel = activitySegmentDomainModel.copy(
            activities = emptyList(),
        )

        val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertEquals(expectedDomainModel, activitySegmentDomainModel)
    }

    @Test
    fun `toDomainModel() should still convert correctly from Data Model to Domain Model when rawActivityType is not defined in the Enums`() {
        val activitySegmentDataModel = activitySegmentDto.copy(
            activityType = "some-strange-activity-type",
        )
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Asia/Tokyo"
        }
        val expectedDomainModel = activitySegmentDomainModel.copy(
            activityType = ActivityType.UNKNOWN_ACTIVITY_TYPE,
            rawActivityType = "some-strange-activity-type",
        )

        val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertEquals(expectedDomainModel, activitySegmentDomainModel)
    }

    @Test
    fun `toDomainModel() should return null if start location is null`() {
        val activitySegmentDataModel = activitySegmentDto.copy(
            startLocation = ActivityLocationDto(),
        )
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Asia/Tokyo"
        }

        val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertNull(activitySegmentDomainModel)
    }

    @Test
    fun `toDomainModel() should return null if end location is null`() {
        val activitySegmentDataModel = activitySegmentDto.copy(
            endLocation = ActivityLocationDto(),
        )
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Asia/Tokyo"
        }

        val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertNull(activitySegmentDomainModel)
    }
}

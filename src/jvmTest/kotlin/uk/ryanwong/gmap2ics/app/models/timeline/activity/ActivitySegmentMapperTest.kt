/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation
import uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegmentDataModelTestData.mockActivitySegment
import us.dustinj.timezonemap.TimeZone

internal class ActivitySegmentMapperTest : FreeSpec() {

    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    // Standard domain model as the baseline
    // Tests only have to modify the necessary properties to highlight test focus
    private val mockActivitySegmentDomainModel = ActivitySegment(
        activities = listOf(
            Activity(
                activityType = ActivityType.IN_PASSENGER_VEHICLE,
                rawActivityType = "IN_PASSENGER_VEHICLE",
            ),
            Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
            Activity(activityType = ActivityType.MOTORCYCLING, rawActivityType = "MOTORCYCLING"),
        ),
        activityType = ActivityType.IN_PASSENGER_VEHICLE,
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

    init {
        "toDomainModel()" - {
            "should convert correctly from ActivitySegment Data Model to Domain Model" {
                // 🔴 Given
                val activitySegmentDataModel = mockActivitySegment
                mockTimeZoneMap = MockTimeZoneMap().apply {
                    mockZoneId = "Asia/Tokyo"
                }
                val expectedDomainModel = mockActivitySegmentDomainModel

                // 🟡 When
                val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

                // 🟢 Then
                activitySegmentDomainModel shouldBe expectedDomainModel
            }

            "should still convert correctly from Data Model to Domain Model when rawActivityType is null" {
                // 🔴 Given
                val activitySegmentDataModel = mockActivitySegment.copy(
                    activityType = null,
                )
                mockTimeZoneMap = MockTimeZoneMap().apply {
                    mockZoneId = "Asia/Tokyo"
                }
                val expectedDomainModel = mockActivitySegmentDomainModel.copy(
                    activityType = ActivityType.UNKNOWN_ACTIVITY_TYPE,
                    rawActivityType = null,
                )

                // 🟡 When
                val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

                // 🟢 Then
                activitySegmentDomainModel shouldBe expectedDomainModel
            }

            "should still convert correctly from Data Model to Domain Model when activities is null" {
                // 🔴 Given
                val activitySegmentDataModel = mockActivitySegment.copy(
                    activities = null,
                )
                mockTimeZoneMap = MockTimeZoneMap().apply {
                    mockZoneId = "Asia/Tokyo"
                }
                val expectedDomainModel = mockActivitySegmentDomainModel.copy(
                    activities = emptyList(),
                )

                // 🟡 When
                val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

                // 🟢 Then
                activitySegmentDomainModel shouldBe expectedDomainModel
            }

            "should still convert correctly from Data Model to Domain Model when rawActivityType is not defined in the Enums" {
                // 🔴 Given
                val activitySegmentDataModel = mockActivitySegment.copy(
                    activityType = "some-strange-activity-type",
                )
                mockTimeZoneMap = MockTimeZoneMap().apply {
                    mockZoneId = "Asia/Tokyo"
                }
                val expectedDomainModel = mockActivitySegmentDomainModel.copy(
                    activityType = ActivityType.UNKNOWN_ACTIVITY_TYPE,
                    rawActivityType = "some-strange-activity-type",
                )

                // 🟡 When
                val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

                // 🟢 Then
                activitySegmentDomainModel shouldBe expectedDomainModel
            }

            "should return null if start location is null" {
                // 🔴 Given
                val activitySegmentDataModel = mockActivitySegment.copy(
                    startLocation = ActivityLocation(),
                )
                mockTimeZoneMap = MockTimeZoneMap().apply {
                    mockZoneId = "Asia/Tokyo"
                }

                // 🟡 When
                val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

                // 🟢 Then
                activitySegmentDomainModel shouldBe null
            }

            "should return null if end location is null" {
                // 🔴 Given
                val activitySegmentDataModel = mockActivitySegment.copy(
                    endLocation = ActivityLocation(),
                )
                mockTimeZoneMap = MockTimeZoneMap().apply {
                    mockZoneId = "Asia/Tokyo"
                }

                // 🟡 When
                val activitySegmentDomainModel = activitySegmentDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

                // 🟢 Then
                activitySegmentDomainModel shouldBe null
            }
        }
    }
}

/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation
import uk.ryanwong.gmap2ics.data.models.timeline.Duration
import uk.ryanwong.gmap2ics.data.models.timeline.RoadSegment
import us.dustinj.timezonemap.TimeZone

internal class ActivitySegmentTest : FreeSpec() {

    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    private val mockActivitySegment = uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegment(
        activities = listOf(
            uk.ryanwong.gmap2ics.data.models.timeline.Activity(
                activityType = "IN_PASSENGER_VEHICLE",
                probability = 99.82574267034934
            ),
            uk.ryanwong.gmap2ics.data.models.timeline.Activity(
                activityType = "WALKING",
                probability = 0.09030052981224143
            ),
            uk.ryanwong.gmap2ics.data.models.timeline.Activity(
                activityType = "MOTORCYCLING",
                probability = 0.04459950826743197
            )
        ),
        activityType = "IN_PASSENGER_VEHICLE",
        confidence = "HIGH",
        distance = 15032,
        duration = Duration(
            endTimestamp = "2019-06-01T01:24:28Z",
            startTimestamp = "2019-06-01T01:04:01Z"
        ),
        endLocation = ActivityLocation(
            address = null,
            latitudeE7 = 344643393,
            locationConfidence = null,
            longitudeE7 = 1324226167,
            name = null,
            placeId = null
        ),
        startLocation = ActivityLocation(
            address = null,
            latitudeE7 = 343970563,
            locationConfidence = null,
            longitudeE7 = 1324677422,
            name = null,
            placeId = null
        ),
        waypointPath = uk.ryanwong.gmap2ics.data.models.timeline.WaypointPath(
            confidence = null,
            distanceMeters = 15444.856340505617,
            roadSegment = listOf(
                RoadSegment(duration = null, placeId = "some-place-id-1"),
                RoadSegment(duration = null, placeId = "some-place-id-2"),
                RoadSegment(duration = null, placeId = "some-place-id-3")
            ),
            source = "INFERRED",
            travelMode = null
        ),
        lastEditedTimestamp = null,
        activityConfidence = null
    )


    init {
        "should convert correctly from ActivitySegment Data Model to App Model" {
            // 🔴 Given
            val activitySegmentDataModel = mockActivitySegment
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Asia/Tokyo"
            }

            // 🟡 When
            val activitySegment = ActivitySegment.from(
                activitySegmentDataModel = activitySegmentDataModel,
                timeZoneMap = mockTimeZoneMap
            )

            // 🟢 Then
            activitySegment shouldBe ActivitySegment(
                activities = listOf(
                    Activity(
                        activityType = ActivityType.IN_PASSENGER_VEHICLE,
                        rawActivityType = "IN_PASSENGER_VEHICLE"
                    ),
                    Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
                    Activity(activityType = ActivityType.MOTORCYCLING, rawActivityType = "MOTORCYCLING")
                ),
                activityType = ActivityType.IN_PASSENGER_VEHICLE,
                rawActivityType = "IN_PASSENGER_VEHICLE",
                distance = 15032,
                durationEndTimestamp = "2019-06-01T01:24:28Z",
                durationStartTimestamp = "2019-06-01T01:04:01Z",
                endLocation = Location(
                    placeId = null,
                    latitudeE7 = 344643393,
                    longitudeE7 = 1324226167,
                    name = null,
                    address = null
                ),
                startLocation = Location(
                    placeId = null,
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = null,
                    address = null
                ),
                waypointPath = WaypointPath(
                    distanceMeters = 15444.856340505617,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                ),
                lastEditedTimestamp = "2019-06-01T01:24:28Z",
                eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
            )
        }

        "should still convert correctly from Data Model to App Model when ActivityType is null" {
            // 🔴 Given
            val activitySegmentDataModel = mockActivitySegment.copy(
                activityType = null
            )
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Asia/Tokyo"
            }

            // 🟡 When
            val activitySegment = ActivitySegment.from(
                activitySegmentDataModel = activitySegmentDataModel,
                timeZoneMap = mockTimeZoneMap
            )

            // 🟢 Then
            activitySegment shouldBe ActivitySegment(
                activities = listOf(
                    Activity(
                        activityType = ActivityType.IN_PASSENGER_VEHICLE,
                        rawActivityType = "IN_PASSENGER_VEHICLE"
                    ),
                    Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
                    Activity(activityType = ActivityType.MOTORCYCLING, rawActivityType = "MOTORCYCLING")
                ),
                activityType = ActivityType.UNKNOWN_ACTIVITY_TYPE,
                rawActivityType = null,
                distance = 15032,
                durationEndTimestamp = "2019-06-01T01:24:28Z",
                durationStartTimestamp = "2019-06-01T01:04:01Z",
                endLocation = Location(
                    placeId = null,
                    latitudeE7 = 344643393,
                    longitudeE7 = 1324226167,
                    name = null,
                    address = null
                ),
                startLocation = Location(
                    placeId = null,
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = null,
                    address = null
                ),
                waypointPath = WaypointPath(
                    distanceMeters = 15444.856340505617,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                ),
                lastEditedTimestamp = "2019-06-01T01:24:28Z",
                eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
            )
        }

        "should still convert correctly from Data Model to App Model when activities is null" {
            // 🔴 Given
            val activitySegmentDataModel = mockActivitySegment.copy(
                activities = null
            )
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Asia/Tokyo"
            }

            // 🟡 When
            val activitySegment = ActivitySegment.from(
                activitySegmentDataModel = activitySegmentDataModel,
                timeZoneMap = mockTimeZoneMap
            )

            // 🟢 Then
            activitySegment shouldBe ActivitySegment(
                activities = emptyList(),
                activityType = ActivityType.IN_PASSENGER_VEHICLE,
                rawActivityType = "IN_PASSENGER_VEHICLE",
                distance = 15032,
                durationEndTimestamp = "2019-06-01T01:24:28Z",
                durationStartTimestamp = "2019-06-01T01:04:01Z",
                endLocation = Location(
                    placeId = null,
                    latitudeE7 = 344643393,
                    longitudeE7 = 1324226167,
                    name = null,
                    address = null
                ),
                startLocation = Location(
                    placeId = null,
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = null,
                    address = null
                ),
                waypointPath = WaypointPath(
                    distanceMeters = 15444.856340505617,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                ),
                lastEditedTimestamp = "2019-06-01T01:24:28Z",
                eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
            )
        }

        "should still convert correctly from Data Model to App Model when ActivityType is not defined in the Enums" {
            // 🔴 Given
            val activitySegmentDataModel = mockActivitySegment.copy(
                activityType = "some-strange-activity-type"
            )
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Asia/Tokyo"
            }

            // 🟡 When
            val activitySegment = ActivitySegment.from(
                activitySegmentDataModel = activitySegmentDataModel,
                timeZoneMap = mockTimeZoneMap
            )

            // 🟢 Then
            activitySegment shouldBe ActivitySegment(
                activities = listOf(
                    Activity(
                        activityType = ActivityType.IN_PASSENGER_VEHICLE,
                        rawActivityType = "IN_PASSENGER_VEHICLE"
                    ),
                    Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
                    Activity(activityType = ActivityType.MOTORCYCLING, rawActivityType = "MOTORCYCLING")
                ),
                activityType = ActivityType.UNKNOWN_ACTIVITY_TYPE,
                rawActivityType = "some-strange-activity-type",
                distance = 15032,
                durationEndTimestamp = "2019-06-01T01:24:28Z",
                durationStartTimestamp = "2019-06-01T01:04:01Z",
                endLocation = Location(
                    placeId = null,
                    latitudeE7 = 344643393,
                    longitudeE7 = 1324226167,
                    name = null,
                    address = null
                ),
                startLocation = Location(
                    placeId = null,
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = null,
                    address = null
                ),
                waypointPath = WaypointPath(
                    distanceMeters = 15444.856340505617,
                    roadSegmentPlaceIds = listOf("some-place-id-1", "some-place-id-2", "some-place-id-3")
                ),
                lastEditedTimestamp = "2019-06-01T01:24:28Z",
                eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
            )
        }

        "should return null if start location is null" {
            // 🔴 Given
            val activitySegmentDataModel = mockActivitySegment.copy(
                startLocation = ActivityLocation()
            )
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Asia/Tokyo"
            }

            // 🟡 When
            val activitySegment = ActivitySegment.from(
                activitySegmentDataModel = activitySegmentDataModel,
                timeZoneMap = mockTimeZoneMap
            )

            // 🟢 Then
            activitySegment shouldBe null
        }

        "should return null if end location is null" {
            // 🔴 Given
            val activitySegmentDataModel = mockActivitySegment.copy(
                endLocation = ActivityLocation()
            )
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Asia/Tokyo"
            }

            // 🟡 When
            val activitySegment = ActivitySegment.from(
                activitySegmentDataModel = activitySegmentDataModel,
                timeZoneMap = mockTimeZoneMap
            )

            // 🟢 Then
            activitySegment shouldBe null
        }
    }
}
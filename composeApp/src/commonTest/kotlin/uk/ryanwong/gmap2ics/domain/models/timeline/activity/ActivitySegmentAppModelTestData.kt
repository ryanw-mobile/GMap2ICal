/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.activity

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.domain.models.ActivityType.FLYING
import uk.ryanwong.gmap2ics.domain.models.ActivityType.IN_BUS
import uk.ryanwong.gmap2ics.domain.models.ActivityType.IN_PASSENGER_VEHICLE
import uk.ryanwong.gmap2ics.domain.models.ActivityType.WALKING
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import us.dustinj.timezonemap.TimeZone

internal object ActivitySegmentAppModelTestData {
    private const val SOME_START_LATITUDE_E7 = 263383300
    private const val SOME_START_LONGITUDE_E7 = 1278000000
    private const val SOME_END_LATITUDE_E7 = 263393300
    private const val SOME_END_LONGITUDE_E7 = 1278500000
    const val SOME_END_DEGREES_LATITUDE = 26.3393300
    const val SOME_END_DEGREES_LONGITUDE = 127.8500000

    val activitySegment = ActivitySegment(
        activities = listOf(
            Activity(activityType = WALKING, rawActivityType = "WALKING"),
            Activity(activityType = IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = IN_BUS, rawActivityType = "IN_BUS"),
        ),
        activityType = FLYING,
        rawActivityType = "FLYING",
        distance = 79,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        endLocation = Location(
            address = null,
            latitudeE7 = SOME_END_LATITUDE_E7,
            longitudeE7 = SOME_END_LONGITUDE_E7,
            name = null,
            placeId = "some-end-place-id",
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = SOME_START_LATITUDE_E7,
            longitudeE7 = SOME_START_LONGITUDE_E7,
            name = null,
            placeId = "some-start-place-id",
        ),
        waypointPath = WaypointPath(
            distanceMeters = 17.61099772105995,
            roadSegmentPlaceIds = listOf(
                "some-road-segment-place-id-1",
                "some-road-segment-place-id-2",
                "some-road-segment-place-id-3",
            ),
        ),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
    )

    val activitySegmentNoStartLocationPlaceId = ActivitySegment(
        activities = listOf(
            Activity(activityType = WALKING, rawActivityType = "WALKING"),
            Activity(activityType = IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = IN_BUS, rawActivityType = "IN_BUS"),
        ),
        activityType = FLYING,
        rawActivityType = "FLYING",
        distance = 79,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        endLocation = Location(
            address = null,
            latitudeE7 = SOME_END_LATITUDE_E7,
            longitudeE7 = SOME_END_LONGITUDE_E7,
            name = null,
            placeId = "some-end-place-id",
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = SOME_START_LATITUDE_E7,
            longitudeE7 = SOME_START_LONGITUDE_E7,
            name = null,
            placeId = null,
        ),
        waypointPath = WaypointPath(
            distanceMeters = 17.61099772105995,
            roadSegmentPlaceIds = listOf(
                "some-road-segment-place-id-1",
                "some-road-segment-place-id-2",
                "some-road-segment-place-id-3",
            ),
        ),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
    )

    val activitySegmentNoEndLocationPlaceId = ActivitySegment(
        activities = listOf(
            Activity(activityType = WALKING, rawActivityType = "WALKING"),
            Activity(activityType = IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = IN_BUS, rawActivityType = "IN_BUS"),
        ),
        activityType = FLYING,
        rawActivityType = "FLYING",
        distance = 79,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        endLocation = Location(
            address = null,
            latitudeE7 = SOME_END_LATITUDE_E7,
            longitudeE7 = SOME_END_LONGITUDE_E7,
            name = null,
            placeId = null,
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = SOME_START_LATITUDE_E7,
            longitudeE7 = SOME_START_LONGITUDE_E7,
            name = null,
            placeId = "some-start-place-id",
        ),
        waypointPath = WaypointPath(
            distanceMeters = 17.61099772105995,
            roadSegmentPlaceIds = listOf(
                "some-road-segment-place-id-1",
                "some-road-segment-place-id-2",
                "some-road-segment-place-id-3",
            ),
        ),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
    )

    val activitySegmentNoWayPoint = ActivitySegment(
        activities = listOf(
            Activity(activityType = WALKING, rawActivityType = "WALKING"),
            Activity(activityType = IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = IN_BUS, rawActivityType = "IN_BUS"),
        ),
        activityType = FLYING,
        rawActivityType = "FLYING",
        distance = 79,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        endLocation = Location(
            address = null,
            latitudeE7 = SOME_END_LATITUDE_E7,
            longitudeE7 = SOME_END_LONGITUDE_E7,
            name = null,
            placeId = "some-end-place-id",
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = SOME_START_LATITUDE_E7,
            longitudeE7 = SOME_START_LONGITUDE_E7,
            name = null,
            placeId = "some-start-place-id",
        ),
        waypointPath = null,
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
    )

    val activitySegmentUKNoWaypoint = ActivitySegment(
        activities = listOf(
            Activity(activityType = WALKING, rawActivityType = "WALKING"),
            Activity(activityType = IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = IN_BUS, rawActivityType = "IN_BUS"),
        ),
        activityType = FLYING,
        rawActivityType = "FLYING",
        distance = 7900,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Europe/London"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Europe/London"),
        endLocation = Location(
            address = null,
            latitudeE7 = SOME_END_LATITUDE_E7,
            longitudeE7 = SOME_END_LONGITUDE_E7,
            name = null,
            placeId = null,
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = SOME_START_LATITUDE_E7,
            longitudeE7 = SOME_START_LONGITUDE_E7,
            name = null,
            placeId = null,
        ),
        waypointPath = null,
        eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
    )
}

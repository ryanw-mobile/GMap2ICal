/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap

internal object ActivitySegmentAppModelTestData {
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()
    const val someStartLatitudeE7 = 263383300
    const val someStartLongitudeE7 = 1278000000
    const val someEndLatitudeE7 = 263393300
    const val someEndLongitudeE7 = 1278500000
    const val someEndDegreesLatitude = 26.3393300
    const val someEndDegreesLongitude = 127.8500000

    val mockActivitySegment = ActivitySegment(
        activities = listOf(
            Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
            Activity(activityType = ActivityType.IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = ActivityType.IN_BUS, rawActivityType = "IN_BUS")
        ),
        activityType = ActivityType.FLYING,
        rawActivityType = "FLYING",
        distance = 79,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        endLocation = Location(
            address = null,
            latitudeE7 = someEndLatitudeE7,
            longitudeE7 = someEndLongitudeE7,
            name = null,
            placeId = "some-end-place-id"
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = someStartLatitudeE7,
            longitudeE7 = someStartLongitudeE7,
            name = null,
            placeId = "some-start-place-id",
        ),
        waypointPath = WaypointPath(
            distanceMeters = 17.61099772105995,
            roadSegmentPlaceIds = listOf(
                "some-road-segment-place-id-1",
                "some-road-segment-place-id-2",
                "some-road-segment-place-id-3"
            )
        ),
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = someEndDegreesLatitude,
            degreesLongitude = someEndDegreesLongitude
        ),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z"
    )
}
/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.activity.Activity
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.WaypointPath
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap

internal object VEventTestData {
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()
    val mockActivitySegment = ActivitySegment(
        activities = listOf(
            Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
            Activity(activityType = ActivityType.IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = ActivityType.IN_BUS, rawActivityType = "IN_BUS")
        ),
        activityType = ActivityType.FLYING,
        rawActivityType = "FLYING",
        distance = 79,
        durationEndTimestamp = "2011-11-11T11:22:22.222Z",
        durationStartTimestamp = "2011-11-11T11:11:11.111Z",
        endLocation = Location(
            address = null,
            latitudeE7 = 263393300,
            longitudeE7 = 1278500000,
            name = null,
            placeId = null
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = 263383300,
            longitudeE7 = 1278000000,
            name = null,
            placeId = null,
        ),
        waypointPath = WaypointPath(
            distanceMeters = 17.61099772105995,
            roadSegmentPlaceIds = emptyList()
        ),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = 26.3383300,
            degreesLongitude = 127.8000000
        )
    )

    val mockChildVisit = ChildVisit(
        durationEndTimestamp = "2011-11-11T11:22:22.222Z",
        durationStartTimestamp = "2011-11-11T11:11:11.111Z",
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-child-visit-place-id",
            latitudeE7 = 263383300,
            longitudeE7 = 1278000000,
            name = "some-name",
            address = "some-address"
        ),
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = 26.3383300,
            degreesLongitude = 127.8000000
        )
    )

    val mockChildVisitPlaceDetails = PlaceDetails(
        placeId = "some-child-visit-place-id",
        name = "some-place-details-name",
        formattedAddress = "some-place-details-formatted-address",
        geo = LatLng(latitude = 26.3383300, longitude = 127.8000000),
        types = listOf(
            "TOURIST_ATTRACTION"
        ),
        url = "https://maps.google.com/?cid=1021876599690425051"
    )

    val mockPlaceVisit = PlaceVisit(
        durationEndTimestamp = "2011-11-11T11:22:22.222Z",
        durationStartTimestamp = "2011-11-11T11:11:11.111Z",
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-place-visit-place-id",
            latitudeE7 = 263383300,
            longitudeE7 = 1278000000,
            name = "some-name",
            address = "some-address"
        ),
        childVisits = emptyList(),
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = 26.3383300,
            degreesLongitude = 127.8000000
        )
    )

    val mockPlaceVisitPlaceDetails = PlaceDetails(
        placeId = "some-place-visit-place-id",
        name = "some-place-details-name",
        formattedAddress = "some-place-details-formatted-address",
        geo = LatLng(latitude = 26.3383300, longitude = 127.8000000),
        types = listOf(
            "TOURIST_ATTRACTION"
        ),
        url = "https://maps.google.com/?cid=1021876599690425051"
    )
}
/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.activity.Activity
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.WaypointPath
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks.MockTimeZoneMap
import us.dustinj.timezonemap.TimeZone

internal object VEventTestData {
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()
    val mockActivitySegment = ActivitySegment(
        activities = listOf(
            Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING"),
            Activity(activityType = ActivityType.IN_PASSENGER_VEHICLE, rawActivityType = "IN_PASSENGER_VEHICLE"),
            Activity(activityType = ActivityType.IN_BUS, rawActivityType = "IN_BUS")
        ),
        activityType = ActivityType.IN_VEHICLE,
        rawActivityType = "IN_VEHICLE",
        distance = 7900,
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        endLocation = Location(
            address = null,
            latitudeE7 = 263393300,
            longitudeE7 = 1278500000,
            name = null,
            placeId = "some-end-location-place-id"
        ),
        startLocation = Location(
            address = null,
            latitudeE7 = 263383300,
            longitudeE7 = 1278000000,
            name = null,
            placeId = "some-start-location-place-id",
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

    val mockActivityFirstSegmentPlaceDetails = PlaceDetails(
        placeId = "some-first-segment-place-id",
        name = "some-first-segment-name",
        formattedAddress = "some-first-segment-formatted-address",
        geo = LatLng(latitude = 26.3383310, longitude = 127.8000010),
        types = listOf(
            "TOURIST_ATTRACTION"
        ),
        url = "https://maps.google.com/?cid=1021876599690425051"
    )
    val mockActivityLastSegmentPlaceDetails = PlaceDetails(
        placeId = "some-last-segment-place-id",
        name = "some-last-segment-name",
        formattedAddress = "some-last-segment-formatted-address",
        geo = LatLng(latitude = 26.3383320, longitude = 127.8000020),
        types = listOf(
            "TOURIST_ATTRACTION"
        ),
        url = "https://maps.google.com/?cid=1021876599690425051"
    )
    val mockActivityStartSegmentPlaceDetails = PlaceDetails(
        placeId = "some-start-segment-place-id",
        name = "some-start-segment-name",
        formattedAddress = "some-start-segment-formatted-address",
        geo = LatLng(latitude = 26.3383330, longitude = 127.8000030),
        types = listOf(
            "TOURIST_ATTRACTION"
        ),
        url = "https://maps.google.com/?cid=1021876599690425051"
    )
    val mockActivityEndSegmentPlaceDetails = PlaceDetails(
        placeId = "some-end-segment-place-id",
        name = "some-end-segment-name",
        formattedAddress = "some-end-segment-formatted-address",
        geo = LatLng(latitude = 26.3383340, longitude = 127.8000040),
        types = listOf(
            "TOURIST_ATTRACTION"
        ),
        url = "https://maps.google.com/?cid=1021876599690425051"
    )
    val mockChildVisit = ChildVisit(
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
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
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-place-visit-place-id",
            latitudeE7 = 263383300,
            longitudeE7 = 1278000000,
            name = "some-name",
            address = "some-address"
        ),
        childVisits = emptyList(),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
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
/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.activity.Activity
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.RoadSegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.WaypointPath
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap

internal class VEventFromActivitySegmentUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Simply ensure the placeDetails is passed to VEvent.from()
     * There are four place lookup actions, but the way we test should be the same:
     *
     * therefore here we just have to test 2 cases:
     * 1. when repository returns something
     * 2. when repository returns nothing (ie. exception).
     *
     * The VEvent.from() conversion, which includes KM/miles and timezones, should be tested under VEventTest
     */

    private lateinit var vEventFromActivitySegmentUseCase: VEventFromActivitySegmentUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()

    private val someStartLatitudeE7 = 263383300
    private val someStartLongitudeE7 = 1278000000
    private val someEndLatitudeE7 = 263393300
    private val someEndLongitudeE7 = 1278500000
    private val someEndDegreesLatitude = 26.3393300
    private val someEndDegreesLongitude = 127.8500000

    private val mockActivitySegment = ActivitySegment(
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
            roadSegment = listOf(
                RoadSegment(placeId = "some-road-segment-place-id-1"),
                RoadSegment(placeId = "some-road-segment-place-id-2"),
                RoadSegment(placeId = "some-road-segment-place-id-3")
            )
        ),
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = someEndDegreesLatitude,
            degreesLongitude = someEndDegreesLongitude
        ),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z"
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        vEventFromActivitySegmentUseCase = VEventFromActivitySegmentUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository
        )
    }

    init {
        "should return correct VEvent if repository returns place details" {
            // 🔴 Given
            setupUseCase()
            mockTimeZoneMap.mockZoneId = "Asia/Tokyo"
            val activitySegment = mockActivitySegment
            val enablePlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                PlaceDetails(
                    placeId = "some-place-id",
                    name = "some-place-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = someEndDegreesLatitude, longitude = someEndDegreesLongitude),
                    types = listOf("ATM"),
                    url = "https://some.url/"
                )
            )

            // 🟡 When
            val vEvent = vEventFromActivitySegmentUseCase(
                activitySegment = activitySegment,
                enablePlacesApiLookup = enablePlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "some-end-place-id",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = "20111111T201111",
                dtEnd = "20111111T202222",
                dtTimeZone = "Asia/Tokyo",
                summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
                location = "some-formatted-address",
                geo = LatLng(latitude = someEndDegreesLatitude, longitude = someEndDegreesLongitude),
                description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
                url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }

        "should still return correct VEvent if repository returns PlaceDetailsNotFoundException" {
            // 🔴 Given
            setupUseCase()
            mockTimeZoneMap.mockZoneId = "Asia/Tokyo"
            val activitySegment = mockActivitySegment
            val enablePlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse =
                Result.failure(exception = PlaceDetailsNotFoundException(placeId = "some-place-id"))

            // 🟡 When
            val vEvent = vEventFromActivitySegmentUseCase(
                activitySegment = activitySegment,
                enablePlacesApiLookup = enablePlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "some-end-place-id",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = "20111111T201111",
                dtEnd = "20111111T202222",
                dtTimeZone = "Asia/Tokyo",
                summary = "✈️ 0.1km ",
                location = "26.33933,127.85",
                geo = LatLng(latitude = someEndDegreesLatitude, longitude = someEndDegreesLongitude),
                description = "Start Location: 26.33833,127.8\\nhttps://maps.google.com?q=26.33833,127.8\\n\\nEnd Location: 26.33933,127.85\\nhttps://maps.google.com?q=26.33933,127.85\\n\\n",
                url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }

        "should still return correct VEvent if repository returns GetPlaceDetailsAPIErrorException" {
            // 🔴 Given
            setupUseCase()
            mockTimeZoneMap.mockZoneId = "Asia/Tokyo"
            val activitySegment = mockActivitySegment
            val enablePlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse =
                Result.failure(exception = GetPlaceDetailsAPIErrorException(apiErrorMessage = "some-api-error-message"))

            // 🟡 When
            val vEvent = vEventFromActivitySegmentUseCase(
                activitySegment = activitySegment,
                enablePlacesApiLookup = enablePlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "some-end-place-id",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = "20111111T201111",
                dtEnd = "20111111T202222",
                dtTimeZone = "Asia/Tokyo",
                summary = "✈️ 0.1km ",
                location = "26.33933,127.85",
                geo = LatLng(latitude = someEndDegreesLatitude, longitude = someEndDegreesLongitude),
                description = "Start Location: 26.33833,127.8\\nhttps://maps.google.com?q=26.33833,127.8\\n\\nEnd Location: 26.33933,127.85\\nhttps://maps.google.com?q=26.33933,127.85\\n\\n",
                url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }
    }
}
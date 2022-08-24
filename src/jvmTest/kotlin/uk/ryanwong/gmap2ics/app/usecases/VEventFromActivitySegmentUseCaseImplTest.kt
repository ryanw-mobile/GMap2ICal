/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegmentAppModelTestData.mockActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegmentAppModelTestData.mockActivitySegmentUKNoWaypoint
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegmentAppModelTestData.someEndDegreesLatitude
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegmentAppModelTestData.someEndDegreesLongitude
import uk.ryanwong.gmap2ics.app.usecases.impl.VEventFromActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.repository.mocks.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.repository.impl.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException

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
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
                location = "some-formatted-address",
                geo = LatLng(latitude = someEndDegreesLatitude, longitude = someEndDegreesLongitude),
                description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
                url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }

        "should still return correct VEvent if ActivitySegment contains no firstPlaceDetails, lastPlaceDetails, startPlaceDetails and endPlaceDetails" {
            // 🔴 Given
            setupUseCase()
            mockTimeZoneMap.mockZoneId = "Europe/London"
            val activitySegment = mockActivitySegmentUKNoWaypoint
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
                placeId = null,
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Europe/London"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Europe/London"),
                summary = "✈️ 4.9mi ",
                location = "26.33933,127.85",
                geo = LatLng(latitude = 26.33933, longitude = 127.85),
                description = "Start Location: 26.33833,127.8\\nhttps://maps.google.com?q=26.33833,127.8\\n\\nEnd Location: 26.33933,127.85\\nhttps://maps.google.com?q=26.33933,127.85\\n\\n",
                url = "https://maps.google.com?q=26.33933,127.85",
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
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "✈️ 0.1km ",
                location = "26.33933,127.85",
                geo = LatLng(latitude = 26.3393300, longitude = 127.8500000),
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
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "✈️ 0.1km ",
                location = "26.33933,127.85",
                geo = LatLng(latitude = 26.3393300, longitude = 127.8500000),
                description = "Start Location: 26.33833,127.8\\nhttps://maps.google.com?q=26.33833,127.8\\n\\nEnd Location: 26.33933,127.85\\nhttps://maps.google.com?q=26.33933,127.85\\n\\n",
                url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }
    }
}
/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.repositories.fakes.FakePlaceDetailsRepository
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegmentAppModelTestData
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegmentAppModelTestData.SOME_END_DEGREES_LATITUDE
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegmentAppModelTestData.SOME_END_DEGREES_LONGITUDE
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegmentAppModelTestData.activitySegment
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromActivitySegmentUseCaseImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class VEventFromActivitySegmentUseCaseImplTest {

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
    private lateinit var fakePlaceDetailsRepository: FakePlaceDetailsRepository

    @BeforeTest
    fun setup() {
        fakePlaceDetailsRepository = FakePlaceDetailsRepository()
        vEventFromActivitySegmentUseCase = VEventFromActivitySegmentUseCaseImpl(
            placeDetailsRepository = fakePlaceDetailsRepository,
        )
    }

    @Test
    fun `returns correct VEvent when repository returns place details`() = runTest {
        val activitySegment = activitySegment
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
            location = "some-formatted-address",
            geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
            description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when activitySegment eventTimeZone is null`() = runTest {
        val activitySegment = activitySegment.copy(
            eventTimeZone = null,
        )
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
            location = "some-formatted-address",
            geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
            description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when enablePlacesApiLookup is false`() = runTest {
        val activitySegment = activitySegment
        val enablePlacesApiLookup = false
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
            location = "some-formatted-address",
            geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
            description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when repository returns PlaceDetailsNotFoundException`() = runTest {
        val activitySegment = activitySegment
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
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
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.failure(exception = PlaceDetailsNotFoundException(placeId = "some-place-id"))

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when repository returns GetPlaceDetailsAPIErrorException`() = runTest {
        val activitySegment = activitySegment
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
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
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.failure(exception = GetPlaceDetailsAPIErrorException(apiErrorMessage = "some-api-error-message"))

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    // Note: firstPlaceDetails and lastPlaceDetails are from the same list
    // Which means if we have an non-empty WayPoint list, both should exist
    @Test
    fun `returns correct VEvent when WayPoint is null`() = runTest {
        val activitySegment = ActivitySegmentAppModelTestData.activitySegmentNoWayPoint
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
            location = "26.33933,127.85",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when startPlaceDetails PlaceId is null`() = runTest {
        val activitySegment = ActivitySegmentAppModelTestData.activitySegmentNoStartLocationPlaceId
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "✈️ 0.1km (null ➡ some-place-name)",
            location = "some-formatted-address",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: 26.33833,127.8\\nhttps://maps.google.com?q=26.33833,127.8\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when endPlaceDetails PlaceId is null`() = runTest {
        val activitySegment = ActivitySegmentAppModelTestData.activitySegmentNoEndLocationPlaceId
        val enablePlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = null,
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "✈️ 0.1km (some-place-name ➡ null)",
            location = "some-formatted-address",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: 26.33933,127.85\\nhttps://maps.google.com?q=26.33933,127.85\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
            url = "https://maps.google.com?q=26.33933,127.85",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = SOME_END_DEGREES_LATITUDE, longitude = SOME_END_DEGREES_LONGITUDE),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromActivitySegmentUseCase(
            activitySegment = activitySegment,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }
}

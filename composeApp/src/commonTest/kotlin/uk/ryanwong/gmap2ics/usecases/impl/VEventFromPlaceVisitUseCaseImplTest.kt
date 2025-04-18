/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import com.esri.core.geometry.Polygon
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.repositories.fakes.FakePlaceDetailsRepository
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromPlaceVisitUseCaseImpl
import us.dustinj.timezonemap.TimeZone
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class VEventFromPlaceVisitUseCaseImplTest {

    /**
     * Test Plan - Simply ensure the placeDetails is passed to VEvent.from()
     * By design the repository can return cached results even enablePlacesApiLookup is false,
     * therefore here we just have to test 2 cases:
     * 1. when repository returns something
     * 2. when repository returns nothing (ie. exception).
     */

    private lateinit var vEventFromPlaceVisitUseCase: VEventFromPlaceVisitUseCaseImpl
    private lateinit var fakePlaceDetailsRepository: FakePlaceDetailsRepository

    // Taking a balance between using numerical values for testing and showing these carries no special meanings.
    private val someLatitudeE7 = 263383300
    private val someLongitudeE7 = 1278000000
    private val someDegreesLatitude = 26.3383300
    private val someDegreesLongitude = 127.8000000

    private val placeVisit = PlaceVisit(
        // meaningless values just to match the format
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-place-id",
            latitudeE7 = someLatitudeE7,
            longitudeE7 = someLongitudeE7,
        ),
        childVisits = emptyList(),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
    )

    @BeforeTest
    fun setup() {
        fakePlaceDetailsRepository = FakePlaceDetailsRepository()
        vEventFromPlaceVisitUseCase = VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = fakePlaceDetailsRepository)
    }

    @Test
    fun `returns correct VEvent when repository returns place details`() = runTest {
        val placeVisit = placeVisit
        val enabledPlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "\uD83C\uDFE7 some-place-name",
            location = "some-formatted-address",
            geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
            description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://some.url/",
            url = "https://some.url/",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromPlaceVisitUseCase(
            placeVisit = placeVisit,
            enablePlacesApiLookup = enabledPlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when placeVisit location placeId is null`() = runTest {
        val placeVisit = placeVisit.copy(
            location = placeVisit.location.copy(
                placeId = null,
            ),
        )
        val enabledPlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = null,
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "📍 null",
            location = "",
            geo = LatLng(latitude = 26.33833, longitude = 127.8),
            description = "Place ID:\\nnull\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:null",
            url = "https://www.google.com/maps/place/?q=place_id:null",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromPlaceVisitUseCase(
            placeVisit = placeVisit,
            enablePlacesApiLookup = enabledPlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when placeVisit eventTimeZone is null`() = runTest {
        val placeVisit = placeVisit.copy(
            eventTimeZone = null,
        )
        val enabledPlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "\uD83C\uDFE7 some-place-name",
            location = "some-formatted-address",
            geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
            description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://some.url/",
            url = "https://some.url/",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromPlaceVisitUseCase(
            placeVisit = placeVisit,
            enablePlacesApiLookup = enabledPlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when enabledPlacesApiLookup is false`() = runTest {
        val placeVisit = placeVisit
        val enabledPlacesApiLookup = false
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "\uD83C\uDFE7 some-place-name",
            location = "some-formatted-address",
            geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
            description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://some.url/",
            url = "https://some.url/",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
            PlaceDetails(
                placeId = "some-place-id",
                name = "some-place-name",
                formattedAddress = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                types = listOf("ATM"),
                url = "https://some.url/",
            ),
        )

        val vEvent = vEventFromPlaceVisitUseCase(
            placeVisit = placeVisit,
            enablePlacesApiLookup = enabledPlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when repository returns PlaceDetailsNotFoundException`() = runTest {
        val placeVisit = placeVisit
        val enabledPlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "\uD83D\uDCCD null",
            location = "",
            geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
            description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id",
            url = "https://www.google.com/maps/place/?q=place_id:some-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.failure(exception = PlaceDetailsNotFoundException(placeId = "some-place-id"))

        val vEvent = vEventFromPlaceVisitUseCase(
            placeVisit = placeVisit,
            enablePlacesApiLookup = enabledPlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct VEvent when repository returns GetPlaceDetailsAPIErrorException`() = runTest {
        val placeVisit = placeVisit
        val enabledPlacesApiLookup = true
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "📍 null",
            location = "",
            geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
            description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id",
            url = "https://www.google.com/maps/place/?q=place_id:some-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        fakePlaceDetailsRepository.getPlaceDetailsResponse = Result.failure(exception = GetPlaceDetailsAPIErrorException(apiErrorMessage = "some-api-error-message"))

        val vEvent = vEventFromPlaceVisitUseCase(
            placeVisit = placeVisit,
            enablePlacesApiLookup = enabledPlacesApiLookup,
        )
        assertEquals(expectedVEvent, vEvent)
    }
}

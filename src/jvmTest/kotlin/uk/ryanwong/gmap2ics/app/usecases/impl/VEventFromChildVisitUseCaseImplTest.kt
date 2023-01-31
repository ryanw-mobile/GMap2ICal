/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.impl

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.data.repository.impl.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.repository.mocks.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import us.dustinj.timezonemap.TimeZone

internal class VEventFromChildVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Very much like how we test PlaceVisitUseCaseImpl, just now we supply ChildVisit,
     * expecting correct VEvent to be returned.
     */

    private lateinit var vEventFromChildVisitUseCase: VEventFromChildVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository

    private val someLatitudeE7 = 263383300
    private val someLongitudeE7 = 1278000000
    private val someDegreesLatitude = 26.3383300
    private val someDegreesLongitude = 127.8000000

    private val mockChildVisit = ChildVisit(
        // meaningless values just to match the format
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "place-id-to-be-kept",
            latitudeE7 = someLatitudeE7,
            longitudeE7 = someLongitudeE7,
            name = "some-name",
            address = "some-address"
        ),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        vEventFromChildVisitUseCase =
            VEventFromChildVisitUseCaseImpl(placeDetailsRepository = mockPlaceDetailsRepository)
    }

    init {
        "should return correct VEvent if repository returns place details" {
            // 🔴 Given
            setupUseCase()
            val childVisit = mockChildVisit
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                PlaceDetails(
                    placeId = "place-id-to-be-kept",
                    name = "some-place-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    types = listOf("ATM"),
                    url = "https://some.url/"
                )
            )
            val expectedVEvent = VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "place-id-to-be-kept",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "\uD83C\uDFE7 some-place-name",
                location = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                url = "https://some.url/",
                lastModified = "2011-11-11T11:22:22.222Z"
            )

            // 🟡 When
            val vEvent = vEventFromChildVisitUseCase(
                childVisit = childVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe expectedVEvent
        }

        "should return correct VEvent if childVisit.location.placeId is null" {
            // 🔴 Given
            setupUseCase()
            val childVisit = mockChildVisit.copy(
                location = mockChildVisit.location.copy(
                    placeId = null
                )
            )
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                PlaceDetails(
                    placeId = "place-id-to-be-kept",
                    name = "some-place-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    types = listOf("ATM"),
                    url = "https://some.url/"
                )
            )
            val expectedVEvent = VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = null,
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "📍 some-name",
                location = "some-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                description = "Place ID:\\nnull\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:null",
                url = "https://www.google.com/maps/place/?q=place_id:null",
                lastModified = "2011-11-11T11:22:22.222Z"
            )

            // 🟡 When
            val vEvent = vEventFromChildVisitUseCase(
                childVisit = childVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe expectedVEvent
        }

        "should return correct VEvent if childVisit.eventTimeZone is null" {
            // 🔴 Given
            setupUseCase()
            val childVisit = mockChildVisit.copy(
                eventTimeZone = null
            )
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                PlaceDetails(
                    placeId = "place-id-to-be-kept",
                    name = "some-place-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    types = listOf("ATM"),
                    url = "https://some.url/"
                )
            )
            val expectedVEvent = VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "place-id-to-be-kept",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "\uD83C\uDFE7 some-place-name",
                location = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                url = "https://some.url/",
                lastModified = "2011-11-11T11:22:22.222Z"
            )

            // 🟡 When
            val vEvent = vEventFromChildVisitUseCase(
                childVisit = childVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe expectedVEvent
        }

        "should return correct VEvent if enabledPlacesApiLookup is false" {
            // 🔴 Given
            setupUseCase()
            val childVisit = mockChildVisit
            val enabledPlacesApiLookup = false
            mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                PlaceDetails(
                    placeId = "place-id-to-be-kept",
                    name = "some-place-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    types = listOf("ATM"),
                    url = "https://some.url/"
                )
            )
            val expectedVEvent = VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "place-id-to-be-kept",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "\uD83C\uDFE7 some-place-name",
                location = "some-formatted-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                url = "https://some.url/",
                lastModified = "2011-11-11T11:22:22.222Z"
            )

            // 🟡 When
            val vEvent = vEventFromChildVisitUseCase(
                childVisit = childVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe expectedVEvent
        }

        "should still return correct VEvent if repository returns PlaceDetailsNotFoundException" {
            // 🔴 Given
            setupUseCase()
            val childVisit = mockChildVisit
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse =
                Result.failure(exception = PlaceDetailsNotFoundException(placeId = "some-place-id"))
            val expectedVEvent = VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "place-id-to-be-kept",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "\uD83D\uDCCD some-name",
                location = "some-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                url = "https://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                lastModified = "2011-11-11T11:22:22.222Z"
            )

            // 🟡 When
            val vEvent = vEventFromChildVisitUseCase(
                childVisit = childVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe expectedVEvent
        }

        "should still return correct VEvent if repository returns GetPlaceDetailsAPIErrorException" {
            // 🔴 Given
            setupUseCase()
            val childVisit = mockChildVisit
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse =
                Result.failure(exception = GetPlaceDetailsAPIErrorException(apiErrorMessage = "some-api-error-message"))
            val expectedVEvent = VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "place-id-to-be-kept",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
                dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
                summary = "📍 some-name",
                location = "some-address",
                geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                url = "https://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                lastModified = "2011-11-11T11:22:22.222Z"
            )

            // 🟡 When
            val vEvent = vEventFromChildVisitUseCase(
                childVisit = childVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe expectedVEvent
        }
    }
}

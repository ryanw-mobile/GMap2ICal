/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import us.dustinj.timezonemap.TimeZone

internal class VEventFromPlaceVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Simply ensure the placeDetails is passed to VEvent.from()
     * By design the repository can return cached results even enablePlacesApiLookup is false,
     * therefore here we just have to test 2 cases:
     * 1. when repository returns something
     * 2. when repository returns nothing (ie. exception).
     */

    private lateinit var vEventFromPlaceVisitUseCase: VEventFromPlaceVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository

    /***
     * Greg, How about this?
     * If we want some numerical values, we name the variables in the "meaningless" way you like,
     * and we still can put some arbitrary values for testing?
     */
    private val someLatitudeE7 = 263383300
    private val someLongitudeE7 = 1278000000
    private val someDegreesLatitude = 26.3383300
    private val someDegreesLongitude = 127.8000000

    private val mockPlaceVisit = PlaceVisit(
        // meaningless values just to match the format
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-place-id",
            latitudeE7 = someLatitudeE7,
            longitudeE7 = someLongitudeE7
        ),
        childVisits = emptyList(),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        vEventFromPlaceVisitUseCase =
            VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = mockPlaceDetailsRepository)
    }

    init {
        "should return correct VEvent if repository returns place details" {
            // 🔴 Given
            setupUseCase()
            val placeVisit = mockPlaceVisit
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                PlaceDetails(
                    placeId = "some-place-id",
                    name = "some-place-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    types = listOf("ATM"),
                    url = "https://some.url/"
                )
            )

            // 🟡 When
            val vEvent = vEventFromPlaceVisitUseCase(
                placeVisit = placeVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
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
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }

        "should still return correct VEvent if repository returns PlaceDetailsNotFoundException" {
            // 🔴 Given
            setupUseCase()
            val placeVisit = mockPlaceVisit
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse =
                Result.failure(exception = PlaceDetailsNotFoundException(placeId = "some-place-id"))

            // 🟡 When
            val vEvent = vEventFromPlaceVisitUseCase(
                placeVisit = placeVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
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
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }

        "should still return correct VEvent if repository returns GetPlaceDetailsAPIErrorException" {
            // 🔴 Given
            setupUseCase()
            val placeVisit = mockPlaceVisit
            val enabledPlacesApiLookup = true
            mockPlaceDetailsRepository.getPlaceDetailsResponse =
                Result.failure(exception = GetPlaceDetailsAPIErrorException(apiErrorMessage = "some-api-error-message"))

            // 🟡 When
            val vEvent = vEventFromPlaceVisitUseCase(
                placeVisit = placeVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
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
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }
    }
}
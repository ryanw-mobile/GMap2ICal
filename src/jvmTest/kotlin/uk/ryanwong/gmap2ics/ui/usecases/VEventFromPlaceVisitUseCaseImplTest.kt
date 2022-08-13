/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.utils.timezonemap.MockTimeZoneMap

internal class VEventFromPlaceVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Simply ensure the placeDetails is passed to VEvent.from()
     * 1. If !enablePlacesApiLookup, just convert it to timeline and return VEvent
     * 2a. If enablePlacesApiLookup, and API returns something, add getPlaceDetails() to timeline then return VEvent
     * 2b. If enablePlacesApiLookup, and API returns no extra details, return VEvent like #1
     */

    private lateinit var vEventFromPlaceVisitUseCase: VEventFromPlaceVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()

    /***
     * Greg, How about this?
     * If we want some numerical values, we name the variables in the way you like,
     * and we still can put some arbitrary values for testing?
     */
    private val someLatitudeE7 = 263383300
    private val someLongitudeE7 = 1278000000
    private val someDegreesLatitude = 26.3383300
    private val someDegreesLongitude = 127.8000000

    private val mockPlaceVisit = PlaceVisit(
        // meaningless values just to match the format
        durationEndTimestamp = "2011-11-11T11:22:22.222Z",
        durationStartTimestamp = "2011-11-11T11:11:11.111Z",
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-place-id",
            // meaningless values just to match the format
            latitudeE7 = someLatitudeE7,
            longitudeE7 = someLongitudeE7
        ),
        childVisits = emptyList(),
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = someDegreesLatitude,
            degreesLongitude = someDegreesLongitude
        )
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        vEventFromPlaceVisitUseCase =
            VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = mockPlaceDetailsRepository)
    }

    init {
        "enablePlacesApiLookup is false" - {
            "should return correct VEvent" {
                // 🔴 Given
                setupUseCase()
                val placeVisit = mockPlaceVisit
                val enabledPlacesApiLookup = false

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
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "📍 null",
                    location = "",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id",
                    url = "https://www.google.com/maps/place/?q=place_id:some-place-id",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }
        }

        "enablePlacesApiLookup is true" - {
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
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "\uD83C\uDFE7 some-place-name",
                    location = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should still return correct VEvent if repository returns no place details" {
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
                        types = listOf("some-place-type"),
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
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "\uD83D\uDCCD some-place-name",
                    location = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should return correct VEvent if repository Place query is failure" {
                // 🔴 Given
                setupUseCase()
                val placeVisit = mockPlaceVisit
                val enabledPlacesApiLookup = true
                mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.failure(exception = Exception())

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
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
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
}
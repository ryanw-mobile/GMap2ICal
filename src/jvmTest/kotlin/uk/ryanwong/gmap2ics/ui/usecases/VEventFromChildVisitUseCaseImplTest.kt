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
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.utils.timezonemap.MockTimeZoneMap

internal class VEventFromChildVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Very much like the ExportPlaceVisitUseCase, but Child Visit contains more optional data fields
     * 1. If !enablePlacesApiLookup, just convert to timeline and then VEvent
     * 2a. If enablePlacesApiLookup, AND API returns something, it should add getPlaceDetails() to timeline and so as for VEvent
     * 2b. If enablePlacesApiLookup, AND API returns nothing, expect getting something the same as #1
     */

    private lateinit var vEventFromChildVisitUseCase: VEventFromChildVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()

    private val someLatitudeE7 = 263383300
    private val someLongitudeE7 = 1278000000
    private val someDegreesLatitude = 26.3383300
    private val someDegreesLongitude = 127.8000000

    private val mockChildVisit = ChildVisit(
        // meaningless values just to match the format
        durationEndTimestamp = "2011-11-11T11:22:22.222Z",
        durationStartTimestamp = "2011-11-11T11:11:11.111Z",
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "place-id-to-be-kept",
            latitudeE7 = someLatitudeE7,
            longitudeE7 = someLongitudeE7,
            name = "some-name",
            address = "some-address"
        ),
        eventTimeZone = mockTimeZoneMap.getOverlappingTimeZone(
            degreesLatitude = someDegreesLatitude,
            degreesLongitude = someDegreesLongitude
        )
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        vEventFromChildVisitUseCase =
            VEventFromChildVisitUseCaseImpl(placeDetailsRepository = mockPlaceDetailsRepository)
    }

    init {
        "enablePlacesApiLookup is false" - {
            "should return correct VEvent" {
                // 🔴 Given
                setupUseCase()
                val childVisit = mockChildVisit
                val enabledPlacesApiLookup = false

                // 🟡 When
                val vEvent = vEventFromChildVisitUseCase(
                    childVisit = childVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "place-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "📍 some-name",
                    location = "some-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    url = "https://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }
        }

        "enablePlacesApiLookup is true" - {
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

                // 🟡 When
                val vEvent = vEventFromChildVisitUseCase(
                    childVisit = childVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "place-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "\uD83C\uDFE7 some-place-name",
                    location = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should still return correct VEvent if repository returns place details with unknown place type" {
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
                        types = listOf("some-place-type"),
                        url = "https://some.url/"
                    )
                )

                // 🟡 When
                val vEvent = vEventFromChildVisitUseCase(
                    childVisit = childVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "place-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "\uD83D\uDCCD some-place-name",
                    location = "some-formatted-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should still return correct VEvent if repository returns no place details"  {
                // 🔴 Given
                setupUseCase()
                val childVisit = mockChildVisit
                val enabledPlacesApiLookup = true
                mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.failure(exception = Exception())

                // 🟡 When
                val vEvent = vEventFromChildVisitUseCase(
                    childVisit = childVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "place-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "📍 some-name",
                    location = "some-address",
                    geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    url = "https://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }
        }
    }
}
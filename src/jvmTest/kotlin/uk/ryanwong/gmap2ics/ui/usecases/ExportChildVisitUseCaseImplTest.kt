/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.LatLng
import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Duration
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Location
import uk.ryanwong.gmap2ics.utils.timezonemap.MockTimeZoneMap

internal class ExportChildVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Very much like the ExportPlaceVisitUseCase, but Child Visit contains more optional data fields
     * 1. If !enablePlacesApiLookup, just convert to timeline and then VEvent
     * 2a. If enablePlacesApiLookup, AND API returns something, it should add getPlaceDetails() to timeline and so as for VEvent
     * 2b. If enablePlacesApiLookup, AND API returns nothing, expect getting something the same as #1
     */

    private lateinit var exportChildVisitUseCase: ExportChildVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()

    private val mockChildVisit = ChildVisit(
        centerLatE7 = 224800000,
        centerLngE7 = 1278000000,
        duration = Duration(startTimestamp = "2011-11-11T11:11:11.111Z", endTimestamp = "2011-11-11T11:22:22.222Z"),
        editConfirmationStatus = "NOT_CONFIRMED",
        lastEditedTimestamp = null,
        location = Location(
            address = "some-address",
            latitudeE7 = 263383300,
            locationConfidence = 70.111111,
            longitudeE7 = 1278000000,
            name = "some-name",
            placeId = "place-id-to-be-kept",
        ),
        locationConfidence = 55,
        placeConfidence = "LOW_CONFIDENCE",
        placeVisitType = "SINGLE_PLACE",
        visitConfidence = 89
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportChildVisitUseCase = ExportChildVisitUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = mockTimeZoneMap
        )
    }

    init {
        "enablePlacesApiLookup is false" - {
            "should return correct VEvent" {
                // 🔴 Given
                setupUseCase()
                val childVisit = mockChildVisit
                val enabledPlacesApiLookup = false
                val ignoredVisitedPlaceIds: List<String> = listOf("place-id-to-be-ignored")

                // 🟡 When
                val vEvent = exportChildVisitUseCase(
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
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    url = "https://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }
        }

        "enablePlacesApiLookup is true" - {
            "should return correct VEvent if repository Place query is success" {
                // 🔴 Given
                setupUseCase()
                val childVisit = mockChildVisit
                val enabledPlacesApiLookup = true
                val ignoredVisitedPlaceIds: List<String> = listOf("place-id-to-be-ignored")
                mockPlaceDetailsRepository.getPlaceResponse = Result.success(
                    Place(
                        placeId = "place-id-to-be-kept",
                        name = "some-place-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 26.3383300, longitude = 127.8),
                        types = listOf("ATM"),
                        url = "https://some.url/"
                    )
                )

                // 🟡 When
                val vEvent = exportChildVisitUseCase(
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
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should still return correct VEvent if repository Place query is success with unknown place type" {
                // 🔴 Given
                setupUseCase()
                val childVisit = mockChildVisit
                val enabledPlacesApiLookup = true
                val ignoredVisitedPlaceIds: List<String> = listOf("place-id-to-be-ignored")
                mockPlaceDetailsRepository.getPlaceResponse = Result.success(
                    Place(
                        placeId = "place-id-to-be-kept",
                        name = "some-place-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 26.3383300, longitude = 127.8),
                        types = listOf("some-place-type"),
                        url = "https://some.url/"
                    )
                )

                // 🟡 When
                val vEvent = exportChildVisitUseCase(
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
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should still return correct VEvent if repository Place query is failure" {
                // 🔴 Given
                setupUseCase()
                val childVisit = mockChildVisit
                val enabledPlacesApiLookup = true
                val ignoredVisitedPlaceIds: List<String> = listOf("place-id-to-be-ignored")
                mockPlaceDetailsRepository.getPlaceResponse = Result.failure(exception = Exception())

                // 🟡 When
                val vEvent = exportChildVisitUseCase(
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
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    url = "https://www.google.com/maps/place/?q=place_id:place-id-to-be-kept",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }
        }
    }
}
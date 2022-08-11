/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.LatLng
import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Duration
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Location
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit
import uk.ryanwong.gmap2ics.utils.timezonemap.MockTimeZoneMap

internal class ExportPlaceVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - expecting VEvent to be returned for all cases
     * 1. If !enablePlacesApiLookup, just convert it to timeline and return VEvent
     * 2a. If enablePlacesApiLookup, and API returns something, add getPlaceDetails() to timeline then return VEvent
     * 2b. If enablePlacesApiLookup, and API returns no extra details, return VEvent like #1
     */

    private lateinit var exportPlaceVisitUseCase: ExportPlaceVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()

    private val mockPlaceVisit = PlaceVisit(
        duration = Duration(
            // meaningless values just to match the format
            startTimestamp = "2011-11-11T11:11:11.111Z",
            endTimestamp = "2011-11-11T11:22:22.222Z"
        ),
        location = Location(
            placeId = "location-id-to-be-kept",
            // meaningless values just to match the format
            latitudeE7 = 263383300,
            longitudeE7 = 1278000000
        )
    )

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportPlaceVisitUseCase = ExportPlaceVisitUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = mockTimeZoneMap
        )
    }

    init {
        "should return correct VEvent if enablePlacesApiLookup is false" {
            // 🔴 Given
            setupUseCase()
            val placeVisit = mockPlaceVisit
            val enabledPlacesApiLookup = false

            // 🟡 When
            val vEvent = exportPlaceVisitUseCase(
                placeVisit = placeVisit,
                enablePlacesApiLookup = enabledPlacesApiLookup
            )

            // 🟢 Then
            vEvent shouldBe VEvent(
                uid = "2011-11-11T11:22:22.222Z",
                placeId = "location-id-to-be-kept",
                dtStamp = "2011-11-11T11:22:22.222Z",
                organizer = null,
                dtStart = "20111111T201111",
                dtEnd = "20111111T202222",
                dtTimeZone = "Asia/Tokyo",
                summary = "📍 null",
                location = "",
                geo = LatLng(latitude = 26.33833, longitude = 127.8),
                description = "Place ID:\\nlocation-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
                url = "https://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
                lastModified = "2011-11-11T11:22:22.222Z"
            )
        }

        "enablePlacesApiLookup is true" - {
            "should return correct VEvent if repository Place query is success" {
                // 🔴 Given
                setupUseCase()
                val placeVisit = mockPlaceVisit
                val enabledPlacesApiLookup = true
                mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                    PlaceDetails(
                        placeId = "location-id-to-be-kept",
                        name = "some-place-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 26.3383300, longitude = 127.8),
                        types = listOf("ATM"),
                        url = "https://some.url/"
                    )
                )

                // 🟡 When
                val vEvent = exportPlaceVisitUseCase(
                    placeVisit = placeVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "location-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "\uD83C\uDFE7 some-place-name",
                    location = "some-formatted-address",
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nlocation-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                    url = "https://some.url/",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }

            "should return correct VEvent if repository Place query is success with unknown place type" {
                // 🔴 Given
                setupUseCase()
                val placeVisit = mockPlaceVisit
                val enabledPlacesApiLookup = true
                mockPlaceDetailsRepository.getPlaceDetailsResponse = Result.success(
                    PlaceDetails(
                        placeId = "location-id-to-be-kept",
                        name = "some-place-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 26.3383300, longitude = 127.8),
                        types = listOf("some-place-type"),
                        url = "https://some.url/"
                    )
                )

                // 🟡 When
                val vEvent = exportPlaceVisitUseCase(
                    placeVisit = placeVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "location-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "\uD83D\uDCCD some-place-name",
                    location = "some-formatted-address",
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nlocation-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
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
                val vEvent = exportPlaceVisitUseCase(
                    placeVisit = placeVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup
                )

                // 🟢 Then
                vEvent shouldBe VEvent(
                    uid = "2011-11-11T11:22:22.222Z",
                    placeId = "location-id-to-be-kept",
                    dtStamp = "2011-11-11T11:22:22.222Z",
                    organizer = null,
                    dtStart = "20111111T201111",
                    dtEnd = "20111111T202222",
                    dtTimeZone = "Asia/Tokyo",
                    summary = "📍 null",
                    location = "",
                    geo = LatLng(latitude = 26.33833, longitude = 127.8),
                    description = "Place ID:\\nlocation-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
                    url = "https://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
                    lastModified = "2011-11-11T11:22:22.222Z"
                )
            }
        }
    }
}
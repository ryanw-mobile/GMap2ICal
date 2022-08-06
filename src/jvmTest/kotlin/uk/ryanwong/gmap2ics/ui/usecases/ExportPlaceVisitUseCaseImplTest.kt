/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import uk.ryanwong.gmap2ics.app.models.LatLng
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Duration
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Location
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

class ExportPlaceVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan
     * 1. If the given placeId is in ignoredVisitedPlaceIds: always return null for all enablePlacesApiLookup cases
     * 2. If !enablePlacesApiLookup, just convert to timeline and then VEvent
     * 3. If enablePlacesApiLookup, either add getPlaceDetails() to timeline then VEvent, else same as #2
     */

    private lateinit var exportPlaceVisitUseCase: ExportPlaceVisitUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository

    private fun setupUseCase() {
        val timeZoneMap = TimeZoneMap.forEverywhere()
        mockkObject(timeZoneMap)
        every { timeZoneMap.getOverlappingTimeZone(any(), any()) } returns TimeZone(
            zoneId = "Asia/Tokyo", // Needs real zone as it affects time calculation
            region = Polygon()
        )
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportPlaceVisitUseCase = ExportPlaceVisitUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = timeZoneMap
        )
    }

    init {
        "enablePlacesApiLookup is false" - {
            "should return null if placeVisit is in ignoredVisitedPlaceIds" {
                // 🔴 Given
                setupUseCase()
                val placeVisit = PlaceVisit(
                    duration = Duration(
                        // meaningless values just to match the format
                        startTimestamp = "2011-11-11T11:11:11.111Z",
                        endTimestamp = "2011-11-11T11:22:22.222Z"
                    ),
                    location = Location(
                        placeId = "location-id-to-be-ignored",
                        // meaningless values just to match the format
                        latitudeE7 = 224800000,
                        longitudeE7 = 1141400000
                    )
                )
                val enabledPlacesApiLookup = false
                val ignoredVisitedPlaceIds: List<String> = listOf("location-id-to-be-ignored")

                // 🟡 When
                val vEvent = exportPlaceVisitUseCase(
                    placeVisit = placeVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup,
                    ignoredVisitedPlaceIds = ignoredVisitedPlaceIds
                )

                // 🟢 Then
                vEvent shouldBe null
            }

            "should return correct VEvent if placeVisit is not in ignoredVisitedPlaceIds" {
                // 🔴 Given
                setupUseCase()
                val placeVisit = PlaceVisit(
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
                val enabledPlacesApiLookup = false
                val ignoredVisitedPlaceIds: List<String> = listOf("location-id-to-be-ignored")

                // 🟡 When
                val vEvent = exportPlaceVisitUseCase(
                    placeVisit = placeVisit,
                    enablePlacesApiLookup = enabledPlacesApiLookup,
                    ignoredVisitedPlaceIds = ignoredVisitedPlaceIds
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
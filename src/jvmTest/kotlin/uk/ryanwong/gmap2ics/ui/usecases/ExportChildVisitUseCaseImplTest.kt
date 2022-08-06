/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockkObject
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

internal class ExportChildVisitUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan - Very much like the ExportPlaceVisitUseCase, but Child Visit contains more optional data fields
     * 1. If the given placeId is in ignoredVisitedPlaceIds: always return null for all enablePlacesApiLookup cases
     * 2. If !enablePlacesApiLookup, just convert to timeline and then VEvent
     * 3. If enablePlacesApiLookup, either add getPlaceDetails() to timeline then VEvent, else same as #2
     */

    lateinit var exportChildVisitUseCase: ExportChildVisitUseCaseImpl
    lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository

    private fun setupUseCase() {
        val timeZoneMap = TimeZoneMap.forEverywhere()
        mockkObject(timeZoneMap)
        every { timeZoneMap.getOverlappingTimeZone(any(), any()) } returns TimeZone(
            zoneId = "Asia/Tokyo", // Needs real zone as it affects time calculation
            region = Polygon()
        )
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportChildVisitUseCase = ExportChildVisitUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = timeZoneMap
        )
    }

    init {


    }
}
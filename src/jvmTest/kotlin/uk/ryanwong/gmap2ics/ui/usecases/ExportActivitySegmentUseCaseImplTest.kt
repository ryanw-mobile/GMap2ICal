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

internal class ExportActivitySegmentUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan
     * 1. If the given activityType is in ignoredActivityType: always throw IgnoredActivityTypeException
     * 2. If the given activityType cannot be resolved, will be assigned  ActivityType.UNKNOWN_ACTIVITY_TYPE and export normally
     * 3. Otherwise should behave normally.
     */

    lateinit var exportActivitySegmentUseCaseImpl: ExportActivitySegmentUseCaseImpl
    lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository

    private fun setupUseCase() {
        val timeZoneMap = TimeZoneMap.forEverywhere()
        mockkObject(timeZoneMap)
        every { timeZoneMap.getOverlappingTimeZone(any(), any()) } returns TimeZone(
            zoneId = "Asia/Tokyo", // Needs real zone as it affects time calculation
            region = Polygon()
        )
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportActivitySegmentUseCaseImpl = ExportActivitySegmentUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = timeZoneMap
        )
    }


    init {


    }
}
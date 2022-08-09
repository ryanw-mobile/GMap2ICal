/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockkObject
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Activity
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivityLocation
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Duration
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.WaypointPath
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

internal class ExportActivitySegmentUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan
     * 1. If the given activityType is in ignoredActivityType: always throw IgnoredActivityTypeException
     * 2. If the given activityType cannot be resolved, will be assigned  ActivityType.UNKNOWN_ACTIVITY_TYPE and export normally
     * 3. "Europe/London" would show distances in miles, otherwise in kilometers
     * 4. Repository if return failure for one or more of (firstPlaceDetail, lastPlaceDetail, startPlaceDetail, endPlaceDetail),
     *    conversion should still behave normally
     * 5. Otherwise, should behave normally.
     */

    private lateinit var exportActivitySegmentUseCase: ExportActivitySegmentUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val timeZoneMap = TimeZoneMap.forEverywhere()

    private val mockActivitySegment = ActivitySegment(
        activities = listOf(
            Activity(activityType = "WALKING", probability = 98.55113383405902),
            Activity(activityType = "IN_PASSENGER_VEHICLE", probability = 1.1125853328056854),
            Activity(activityType = "IN_BUS", probability = 0.3006288883431215)
        ),
        activityType = "FLYING",
        confidence = "HIGH",
        distance = 79,
        duration = Duration(endTimestamp = "2011-11-11T11:22:22.222Z", startTimestamp = "2011-11-11T11:11:11.111Z"),
        endLocation = ActivityLocation(
            address = null,
            latitudeE7 = 263393300,
            locationConfidence = null,
            longitudeE7 = 1278500000,
            name = null,
            placeId = null
        ),
        startLocation = ActivityLocation(
            address = null,
            latitudeE7 = 263383300,
            locationConfidence = null,
            longitudeE7 = 1278000000,
            name = null,
            placeId = null,
        ),
        waypointPath = WaypointPath(
            confidence = 1.0,
            distanceMeters = 17.61099772105995,
            roadSegment = null,
            source = "BACKFILLED",
            travelMode = "WALK"
        ),
        lastEditedTimestamp = null,
        activityConfidence = null
    )

    private fun setupUseCase(zoneId: String) {
        mockkObject(timeZoneMap)
        every { timeZoneMap.getOverlappingTimeZone(any(), any()) } returns TimeZone(
            zoneId = zoneId, // Needs real zone as it affects time calculation
            region = Polygon()
        )
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportActivitySegmentUseCase = ExportActivitySegmentUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = timeZoneMap
        )
    }

    init {
        "should return IgnoredActivityTypeException if activityType is in ignoredActivityType" {
            // 🔴 Given
            setupUseCase(zoneId = "Asia/Tokyo")
            val activitySegment = mockActivitySegment
            val ignoredActivityType: List<ActivityType> = listOf(ActivityType.FLYING)

            // 🟡 When
            val result = exportActivitySegmentUseCase(
                activitySegment = activitySegment,
                ignoredActivityType = ignoredActivityType
            )

            // 🟢 Then
            with(result) {
                isFailure shouldBe true
                exceptionOrNull().shouldBeTypeOf<IgnoredActivityTypeException>()
            }
        }
    }
}
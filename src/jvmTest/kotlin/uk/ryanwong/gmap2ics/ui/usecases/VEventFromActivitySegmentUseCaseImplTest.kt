/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.kotest.core.spec.style.FreeSpec
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Activity
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivityLocation
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Duration
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.WaypointPath
import uk.ryanwong.gmap2ics.utils.timezonemap.MockTimeZoneMap

internal class VEventFromActivitySegmentUseCaseImplTest : FreeSpec() {

    /**
     * Test Plan -- to be revised
     * 1. If the given activityType is in ignoredActivityType: always throw IgnoredActivityTypeException
     * 2. If the given activityType cannot be resolved, will be assigned  ActivityType.UNKNOWN_ACTIVITY_TYPE and export normally
     * 3. "Europe/London" would show distances in miles, otherwise in kilometers
     * 4. Repository if return failure for one or more of (firstPlaceDetail, lastPlaceDetail, startPlaceDetail, endPlaceDetail),
     *    conversion should still behave normally
     * 5. Otherwise, should behave normally.
     */

    private lateinit var vEventFromActivitySegmentUseCase: VEventFromActivitySegmentUseCaseImpl
    private lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository
    private val mockTimeZoneMap: MockTimeZoneMap = MockTimeZoneMap()

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

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        vEventFromActivitySegmentUseCase = VEventFromActivitySegmentUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = mockTimeZoneMap
        )
    }

    init {
//        "should return IgnoredActivityTypeException if activityType is in ignoredActivityType" {
//            // 🔴 Given
//            setupUseCase()
//            mockTimeZoneMap.mockZoneId = "Asia/Tokyo"
//            val activitySegment = mockActivitySegment
//            val ignoredActivityType: List<ActivityType> = listOf(ActivityType.FLYING)
//
//            // 🟡 When
//            val result = vEventFromActivitySegmentUseCase(
//                activitySegment = activitySegment
//            )
//
//            // 🟢 Then
//            with(result) {
//                isFailure shouldBe true
//                exceptionOrNull().shouldBeTypeOf<IgnoredActivityTypeException>()
//            }
//        }
    }
}
/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.domain.models.ActivityType
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.VEventTestData
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegmentAppModelTestData
import uk.ryanwong.gmap2ics.domain.usecases.GetActivitySegmentVEventUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromActivitySegmentUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class GetActivitySegmentVEventUseCaseImplTest {

    private lateinit var getActivitySegmentVEventUseCase: GetActivitySegmentVEventUseCase
    private lateinit var fakeVEventFromActivitySegmentUseCase: FakeVEventFromActivitySegmentUseCase

    private val expectedVEvent = VEvent(
        uid = "2011-11-11T11:22:22.222Z",
        placeId = "some-end-place-id",
        dtStamp = "2011-11-11T11:22:22.222Z",
        organizer = null,
        dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        summary = "✈️ 0.1km (some-place-name ➡ some-place-name)",
        location = "some-formatted-address",
        geo = LatLng(
            latitude = ActivitySegmentAppModelTestData.SOME_END_DEGREES_LATITUDE,
            longitude = ActivitySegmentAppModelTestData.SOME_END_DEGREES_LONGITUDE,
        ),
        description = "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-place-id\\n\\nEnd Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-place-id\\n\\nFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n",
        url = "https://www.google.com/maps/place/?q=place_id:some-end-place-id",
        lastModified = "2011-11-11T11:22:22.222Z",
    )

    @BeforeTest
    fun setup() {
        fakeVEventFromActivitySegmentUseCase = FakeVEventFromActivitySegmentUseCase()
        getActivitySegmentVEventUseCase = GetActivitySegmentVEventUseCaseImpl(
            vEventFromActivitySegmentUseCase = fakeVEventFromActivitySegmentUseCase,
        )
    }

    @Test
    fun `returns VEvent when enablePlaceApiLookup is true and ActivitySegment not in ignored list`() = runTest {
        val activitySegment = VEventTestData.activitySegment // Conversion is faked so doesn't matter
        val ignoredActivityType = listOf(ActivityType.STILL)
        val enablePlacesApiLookup = true
        fakeVEventFromActivitySegmentUseCase.useCaseResponse = expectedVEvent

        val actualVEvent = getActivitySegmentVEventUseCase(
            activitySegment = activitySegment,
            ignoredActivityType = ignoredActivityType,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertEquals(expectedVEvent, actualVEvent)
    }

    @Test
    fun `returns VEvent when enablePlaceApiLookup is false and ActivitySegment not in ignored list`() = runTest {
        val activitySegment = VEventTestData.activitySegment
        val ignoredActivityType = listOf(ActivityType.STILL)
        val enablePlacesApiLookup = false
        fakeVEventFromActivitySegmentUseCase.useCaseResponse = expectedVEvent

        val actualVEvent = getActivitySegmentVEventUseCase(
            activitySegment = activitySegment,
            ignoredActivityType = ignoredActivityType,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertEquals(expectedVEvent, actualVEvent)
    }

    @Test
    fun `returns null when ActivitySegment is in ignored list`() = runTest {
        val activitySegment = VEventTestData.activitySegment // ActivityType inside matters
        val ignoredActivityType = listOf(ActivityType.IN_VEHICLE)
        val enablePlacesApiLookup = true
        fakeVEventFromActivitySegmentUseCase.useCaseResponse = expectedVEvent

        val actualVEvent = getActivitySegmentVEventUseCase(
            activitySegment = activitySegment,
            ignoredActivityType = ignoredActivityType,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertNull(actualVEvent)
    }
}

/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.domain.models.ActivityType
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.VEventTestData
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegmentAppModelTestData
import uk.ryanwong.gmap2ics.domain.usecases.GetActivitySegmentVEventUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromActivitySegmentUseCase

class GetActivitySegmentVEventUseCaseImplTest : FreeSpec() {

    private lateinit var getActivitySegmentVEventUseCase: GetActivitySegmentVEventUseCase
    private lateinit var fakeVEventFromActivitySegmentUseCase: FakeVEventFromActivitySegmentUseCase

    private val vEvent = VEvent(
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

    init {
        beforeTest {
            fakeVEventFromActivitySegmentUseCase = FakeVEventFromActivitySegmentUseCase()
            getActivitySegmentVEventUseCase = GetActivitySegmentVEventUseCaseImpl(
                vEventFromActivitySegmentUseCase = fakeVEventFromActivitySegmentUseCase,
            )
        }

        "Should return VEvent correctly if ActivitySegment supplied is not in the ignoredActivityType" - {
            "When enablePlaceApiLookup is true" {
                val activitySegment = VEventTestData.activitySegment // Conversion is faked so doesn't matter
                val ignoredActivityType = listOf(ActivityType.STILL)
                val enablePlacesApiLookup = true
                fakeVEventFromActivitySegmentUseCase.useCaseResponse = vEvent

                val vEvent = getActivitySegmentVEventUseCase(
                    activitySegment = activitySegment,
                    ignoredActivityType = ignoredActivityType,
                    enablePlacesApiLookup = enablePlacesApiLookup,
                )

                vEvent shouldBe this@GetActivitySegmentVEventUseCaseImplTest.vEvent
            }

            "When enablePlaceApiLookup is false" {
                val activitySegment = VEventTestData.activitySegment
                val ignoredActivityType = listOf(ActivityType.STILL)
                val enablePlacesApiLookup = false
                fakeVEventFromActivitySegmentUseCase.useCaseResponse = vEvent

                val vEvent = getActivitySegmentVEventUseCase(
                    activitySegment = activitySegment,
                    ignoredActivityType = ignoredActivityType,
                    enablePlacesApiLookup = enablePlacesApiLookup,
                )

                vEvent shouldBe this@GetActivitySegmentVEventUseCaseImplTest.vEvent
            }
        }

        "Should return null if ActivitySegment supplied is in the ignoredActivityType" {
            val activitySegment = VEventTestData.activitySegment // ActivityType inside matters
            val ignoredActivityType = listOf(ActivityType.IN_VEHICLE)
            val enablePlacesApiLookup = true
            fakeVEventFromActivitySegmentUseCase.useCaseResponse = vEvent

            val vEvent = getActivitySegmentVEventUseCase(
                activitySegment = activitySegment,
                ignoredActivityType = ignoredActivityType,
                enablePlacesApiLookup = enablePlacesApiLookup,
            )

            vEvent shouldBe null
        }
    }
}

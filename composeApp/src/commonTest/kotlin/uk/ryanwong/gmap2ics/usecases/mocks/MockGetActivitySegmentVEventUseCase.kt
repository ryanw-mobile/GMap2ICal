/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.mocks

import uk.ryanwong.gmap2ics.domain.models.ActivityType
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.domain.usecases.GetActivitySegmentVEventUseCase

class MockGetActivitySegmentVEventUseCase : GetActivitySegmentVEventUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        enablePlacesApiLookup: Boolean,
    ): VEvent? {
        return mockUseCaseResponse
    }
}

/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment

class MockGetActivitySegmentVEventUseCase : GetActivitySegmentVEventUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        return mockUseCaseResponse
    }
}
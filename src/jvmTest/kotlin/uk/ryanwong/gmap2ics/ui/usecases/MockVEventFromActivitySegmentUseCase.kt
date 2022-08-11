/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment

class MockVEventFromActivitySegmentUseCase : VEventFromActivitySegmentUseCase {
    var mockUseCaseResponse: Result<Pair<VEvent, String?>>? = null
    override suspend fun invoke(activitySegment: ActivitySegment): Result<Pair<VEvent, String?>> {
        return mockUseCaseResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}
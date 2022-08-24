/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.mocks

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.usecases.VEventFromActivitySegmentUseCase

class MockVEventFromActivitySegmentUseCase : VEventFromActivitySegmentUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(activitySegment: ActivitySegment, enablePlacesApiLookup: Boolean): VEvent {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }
}
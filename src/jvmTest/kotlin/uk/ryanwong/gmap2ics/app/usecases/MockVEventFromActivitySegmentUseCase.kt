/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.usecases.VEventFromActivitySegmentUseCase

class MockVEventFromActivitySegmentUseCase(private val mockUseCaseResponse: VEvent) : VEventFromActivitySegmentUseCase {
    override suspend fun invoke(activitySegment: ActivitySegment, enablePlacesApiLookup: Boolean): VEvent {
        return mockUseCaseResponse
    }
}
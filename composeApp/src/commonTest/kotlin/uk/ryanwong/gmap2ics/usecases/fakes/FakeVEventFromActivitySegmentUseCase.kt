/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.fakes

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.VEventFromActivitySegmentUseCase

class FakeVEventFromActivitySegmentUseCase : VEventFromActivitySegmentUseCase {
    var useCaseResponse: VEvent? = null
    override suspend fun invoke(activitySegment: ActivitySegment, enablePlacesApiLookup: Boolean): VEvent = useCaseResponse ?: throw Exception("response not defined")
}

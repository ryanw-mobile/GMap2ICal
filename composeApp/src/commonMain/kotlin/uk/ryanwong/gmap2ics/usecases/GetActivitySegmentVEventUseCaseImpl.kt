/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases

import uk.ryanwong.gmap2ics.domain.models.ActivityType
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.domain.usecases.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromActivitySegmentUseCase

class GetActivitySegmentVEventUseCaseImpl(
    private val vEventFromActivitySegmentUseCase: VEventFromActivitySegmentUseCase,
) : GetActivitySegmentVEventUseCase {
    override suspend fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        enablePlacesApiLookup: Boolean,
    ): VEvent? {
        return if (ignoredActivityType.contains(activitySegment.activityType)) {
            null
        } else {
            return vEventFromActivitySegmentUseCase(
                activitySegment = activitySegment,
                enablePlacesApiLookup = enablePlacesApiLookup,
            )
        }
    }
}

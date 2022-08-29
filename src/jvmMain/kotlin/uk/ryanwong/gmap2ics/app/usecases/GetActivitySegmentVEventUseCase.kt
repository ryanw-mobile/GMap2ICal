/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment

interface GetActivitySegmentVEventUseCase {
    suspend operator fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        enablePlacesApiLookup: Boolean
    ): VEvent?
}

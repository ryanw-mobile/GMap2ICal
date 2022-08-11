/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment

interface VEventFromActivitySegmentUseCase {
    suspend operator fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>
    ): Result<Pair<VEvent, String?>>
}
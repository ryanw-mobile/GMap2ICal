/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import kotlinx.coroutines.flow.StateFlow
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment

class MockExportActivitySegmentUseCase: ExportActivitySegmentUseCase {
    override val statusLog: StateFlow<String?>
        get() = TODO("Not yet implemented")

    override suspend fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        verboseLogs: Boolean
    ): Result<VEvent> {
        TODO("Not yet implemented")
    }
}
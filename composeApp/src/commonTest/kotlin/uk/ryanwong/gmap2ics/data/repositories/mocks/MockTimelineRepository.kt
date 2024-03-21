/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories.mocks

import uk.ryanwong.gmap2ics.domain.models.timeline.Timeline
import uk.ryanwong.gmap2ics.domain.repositories.TimelineRepository

class MockTimelineRepository : TimelineRepository {
    var getTimeLineResponse: Result<Timeline>? = null
    override suspend fun getTimeLine(filePath: String): Result<Timeline> {
        return getTimeLineResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}

/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.timeline.Timeline

class MockTimelineRepository : TimelineRepository {
    var parseTimeLineResponse: Result<Timeline>? = null
    override suspend fun getTimeLine(filePath: String): Result<Timeline> {
        return parseTimeLineResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}
/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects

class MockTimelineRepository : TimelineRepository {
    var parseTimeLineResponse: Result<TimelineObjects>? = null
    override suspend fun getTimeLine(filePath: String): Result<TimelineObjects> {
        return parseTimeLineResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}
/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects

class MockTimelineRepository : TimelineRepository {

    var getEventListResponse: Result<List<VEvent>>? = null
    override suspend fun getEventList(filePath: String): Result<List<VEvent>> {
        return getEventListResponse ?: Result.failure(Exception("mock response unavailable"))
    }

    var parseTimeLineResponse: Result<TimelineObjects>? = null
    override suspend fun parseTimeLine(filePath: String): Result<TimelineObjects> {
        return parseTimeLineResponse ?: Result.failure(Exception("mock response unavailable"))
    }

    var processActivitySegmentResponse: Result<TimelineItem>? = null
    override suspend fun processActivitySegment(activitySegment: ActivitySegment): Result<TimelineItem> {
        return processActivitySegmentResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}
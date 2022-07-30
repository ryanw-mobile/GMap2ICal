package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects

class MockTimelineRepository : TimelineRepository {

    var getEventListResponse: List<VEvent>? = null
    override suspend fun getEventList(filePath: String): List<VEvent> {
        return getEventListResponse ?: emptyList()
    }

    var parseTimeLineResponse: TimelineObjects? = null
    override fun parseTimeLine(filePath: String): TimelineObjects? {
        return parseTimeLineResponse
    }

    var processActivitySegmentResponse: TimelineItem? = null
    override suspend fun processActivitySegment(activitySegment: ActivitySegment): TimelineItem? {
        return processActivitySegmentResponse
    }
}
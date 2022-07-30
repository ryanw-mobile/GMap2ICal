package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects

interface TimelineRepository {
    suspend fun getEventList(filePath: String): Result<List<VEvent>>
    suspend fun parseTimeLine(filePath: String): Result<TimelineObjects>
    suspend fun processActivitySegment(activitySegment: ActivitySegment): Result<TimelineItem>
}
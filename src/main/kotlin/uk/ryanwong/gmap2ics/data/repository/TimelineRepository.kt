package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.domain.models.TimelineItem
import uk.ryanwong.gmap2ics.domain.models.VEvent

interface TimelineRepository {
    suspend fun getEventList(filePath: String): List<VEvent>

    fun parseTimeLine(filePath: String): TimelineObjects?

    suspend fun processActivitySegment(activitySegment: ActivitySegment): TimelineItem?
}
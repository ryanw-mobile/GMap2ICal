/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.app.models.timeline.activity.toDomainModel
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.toDomainModel
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects

data class Timeline(
    val timelineEntries: List<TimelineEntry>,
) {
    companion object {
        fun from(
            timelineObjects: TimelineObjects,
            timeZoneMap: TimeZoneMapWrapper,
        ): Timeline {
            return Timeline(
                timelineEntries = timelineObjects.timelineObjects?.map { timelineObject ->
                    TimelineEntry(
                        activitySegment = timelineObject.activitySegment?.toDomainModel(timeZoneMap = timeZoneMap),
                        placeVisit = timelineObject.placeVisit?.toDomainModel(timeZoneMap = timeZoneMap),
                    )
                } ?: emptyList(),
            )
        }
    }
}

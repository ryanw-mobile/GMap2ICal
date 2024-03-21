/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.toDomainModel
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.toDomainModel

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

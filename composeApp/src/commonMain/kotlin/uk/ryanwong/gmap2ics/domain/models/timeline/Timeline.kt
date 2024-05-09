/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjectsDto
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.toDomainModel
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.toDomainModel
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.TimeZoneMapWrapper

data class Timeline(
    val timelineEntries: List<TimelineEntry>,
) {
    companion object {
        fun from(
            timelineObjectsDto: TimelineObjectsDto,
            timeZoneMap: TimeZoneMapWrapper,
        ): Timeline {
            return Timeline(
                timelineEntries = timelineObjectsDto.timelineObjects?.map { timelineObject ->
                    TimelineEntry(
                        activitySegment = timelineObject.activitySegment?.toDomainModel(timeZoneMap = timeZoneMap),
                        placeVisit = timelineObject.placeVisit?.toDomainModel(timeZoneMap = timeZoneMap),
                    )
                } ?: emptyList(),
            )
        }
    }
}

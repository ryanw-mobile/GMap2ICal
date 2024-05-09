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
                timelineEntries = timelineObjectsDto.timelineObjectDtos?.map { timelineObject ->
                    TimelineEntry(
                        activitySegment = timelineObject.activitySegmentDto?.toDomainModel(timeZoneMap = timeZoneMap),
                        placeVisit = timelineObject.placeVisitDto?.toDomainModel(timeZoneMap = timeZoneMap),
                    )
                } ?: emptyList(),
            )
        }
    }
}

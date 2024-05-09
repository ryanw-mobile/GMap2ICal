/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.placevisit

import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.toDomainModel
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.TimeZoneMapWrapper

fun uk.ryanwong.gmap2ics.data.models.timeline.ChildVisitDto.toDomainModel(timeZoneMap: TimeZoneMapWrapper): ChildVisit? {
    //  If a child visit does not have a valid duration start & end, we simply drop it during conversion
    val locationDomainModel = location.toDomainModel()

    return if (duration == null || locationDomainModel == null) {
        null
    } else {
        val eventTimeZone = timeZoneMap.getOverlappingTimeZone(
            degreesLatitude = locationDomainModel.getLatitude(),
            degreesLongitude = locationDomainModel.getLongitude(),
        )
        ChildVisit(
            durationEndTimestamp = RawTimestamp(
                timestamp = duration.endTimestamp,
                timezoneId = eventTimeZone?.zoneId ?: "UTC",
            ),
            durationStartTimestamp = RawTimestamp(
                timestamp = duration.startTimestamp,
                timezoneId = eventTimeZone?.zoneId ?: "UTC",
            ),
            lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
            location = locationDomainModel,
            eventTimeZone = eventTimeZone,
        )
    }
}

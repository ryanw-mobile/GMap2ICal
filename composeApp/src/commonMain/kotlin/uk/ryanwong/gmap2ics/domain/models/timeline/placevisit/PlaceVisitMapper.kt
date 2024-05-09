/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.placevisit

import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.toDomainModel
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.TimeZoneMapWrapper

fun uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisitDto.toDomainModel(timeZoneMap: TimeZoneMapWrapper): PlaceVisit? {
    val locationDomainModel = location.toDomainModel()

    return locationDomainModel?.let {
        val eventTimeZone = timeZoneMap.getOverlappingTimeZone(
            degreesLatitude = locationDomainModel.getLatitude(),
            degreesLongitude = locationDomainModel.getLongitude(),
        )

        PlaceVisit(
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
            childVisits = childVisits?.mapNotNull { childVisitDataModel ->
                childVisitDataModel.toDomainModel(timeZoneMap = timeZoneMap)
            } ?: emptyList(),
            eventTimeZone = eventTimeZone,
        )
    }
}

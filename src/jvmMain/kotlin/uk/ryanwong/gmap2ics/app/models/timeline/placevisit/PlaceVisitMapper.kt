/*
 * Copyright (c) 2023. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.toDomainModel
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper

fun uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisit.toDomainModel(timeZoneMap: TimeZoneMapWrapper): PlaceVisit? {
    val locationDomainModel = location.toDomainModel()

    return locationDomainModel?.let {
        val eventTimeZone = timeZoneMap.getOverlappingTimeZone(
            degreesLatitude = locationDomainModel.getLatitude(),
            degreesLongitude = locationDomainModel.getLongitude()
        )

        PlaceVisit(
            durationEndTimestamp = RawTimestamp(
                timestamp = duration.endTimestamp,
                timezoneId = eventTimeZone?.zoneId ?: "UTC"
            ),
            durationStartTimestamp = RawTimestamp(
                timestamp = duration.startTimestamp,
                timezoneId = eventTimeZone?.zoneId ?: "UTC"
            ),
            lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
            location = locationDomainModel,
            childVisits = childVisits?.mapNotNull { childVisitDataModel ->
                childVisitDataModel.toDomainModel(timeZoneMap = timeZoneMap)
            } ?: emptyList(),
            eventTimeZone = eventTimeZone
        )
    }
}

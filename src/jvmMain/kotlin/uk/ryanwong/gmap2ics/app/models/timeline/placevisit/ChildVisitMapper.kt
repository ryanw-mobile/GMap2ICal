/*
 * Copyright (c) 2023. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.toDomainModel
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper

fun uk.ryanwong.gmap2ics.data.models.timeline.ChildVisit.toDomainModel(timeZoneMap: TimeZoneMapWrapper): ChildVisit? {
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

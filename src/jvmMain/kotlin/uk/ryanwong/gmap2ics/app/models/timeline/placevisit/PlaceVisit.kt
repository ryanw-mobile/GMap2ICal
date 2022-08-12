/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

data class PlaceVisit(
    val durationEndTimestamp: String,
    val durationStartTimestamp: String,
    val lastEditedTimestamp: String,
    val location: Location,
    val childVisits: List<ChildVisit> = emptyList(),
    val eventTimeZone: TimeZone?
) {
    companion object {
        fun from(
            placeVisitDataModel: uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit,
            timeZoneMap: TimeZoneMapWrapper
        ): PlaceVisit {
            with(placeVisitDataModel) {
                return PlaceVisit(
                    durationEndTimestamp = duration.endTimestamp,
                    durationStartTimestamp = duration.startTimestamp,
                    lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
                    location = Location.from(locationDataModel = location),
                    childVisits = childVisits?.mapNotNull { childVisit ->
                        ChildVisit.from(childVisitDataModel = childVisit, timeZoneMap = timeZoneMap)
                    } ?: emptyList(),
                    eventTimeZone = timeZoneMap.getOverlappingTimeZone(
                        degreesLatitude = location.latitudeE7 * 0.0000001,
                        degreesLongitude = location.longitudeE7 * 0.0000001
                    )
                )
            }
        }
    }
}
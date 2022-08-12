/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

data class ChildVisit(
    val durationEndTimestamp: String,
    val durationStartTimestamp: String,
    val lastEditedTimestamp: String,
    val location: Location,
    val eventTimeZone: TimeZone?
) {
    companion object {
        fun from(
            childVisitDataModel: uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit,
            timeZoneMap: TimeZoneMapWrapper
        ): ChildVisit? {
            with(childVisitDataModel) {
                //  If a child visit does not have a valid duration start & end, we simply drop it during conversion
                return if (duration == null) {
                    null

                } else {
                    ChildVisit(
                        durationEndTimestamp = duration.endTimestamp,
                        durationStartTimestamp = duration.startTimestamp,
                        lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
                        location = Location.from(locationDataModel = location),
                        eventTimeZone = timeZoneMap.getOverlappingTimeZone(
                            degreesLatitude = location.latitudeE7 * 0.0000001,
                            degreesLongitude = location.longitudeE7 * 0.0000001
                        )
                    )
                }
            }
        }
    }
}
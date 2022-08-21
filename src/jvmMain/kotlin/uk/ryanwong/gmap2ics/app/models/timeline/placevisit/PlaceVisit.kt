/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

data class PlaceVisit(
    val durationEndTimestamp: RawTimestamp,
    val durationStartTimestamp: RawTimestamp,
    val lastEditedTimestamp: String,
    val location: Location,
    val childVisits: List<ChildVisit> = emptyList(),
    val eventTimeZone: TimeZone?
) {
    companion object {
        fun from(
            placeVisitDataModel: uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisit,
            timeZoneMap: TimeZoneMapWrapper
        ): PlaceVisit? {
            with(placeVisitDataModel) {
                val locationAppModel = Location.from(locationDataModel = location)

                return locationAppModel?.let {
                    val eventTimeZone = timeZoneMap.getOverlappingTimeZone(
                        degreesLatitude = locationAppModel.getLatitude(),
                        degreesLongitude = locationAppModel.getLongitude()
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
                        location = locationAppModel,
                        childVisits = childVisits?.mapNotNull { childVisit ->
                            ChildVisit.from(childVisitDataModel = childVisit, timeZoneMap = timeZoneMap)
                        } ?: emptyList(),
                        eventTimeZone = eventTimeZone
                    )
                }
            }
        }
    }
}
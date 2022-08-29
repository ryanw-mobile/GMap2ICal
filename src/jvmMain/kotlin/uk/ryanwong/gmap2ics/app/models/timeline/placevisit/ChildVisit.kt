/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

/***
 * PlaceVisit and ChildVisit look pretty similar,
 * but I decided not to make them tightly coupled by applying inheritance.
 * Because by design even they look similar, nobody says they share the same behavior.
 *
 * Tightly coupling is a sin nowadays.
 */
data class ChildVisit(
    val durationEndTimestamp: RawTimestamp,
    val durationStartTimestamp: RawTimestamp,
    val lastEditedTimestamp: String,
    val location: Location,
    val eventTimeZone: TimeZone?
) {
    companion object {
        fun from(
            childVisitDataModel: uk.ryanwong.gmap2ics.data.models.timeline.ChildVisit,
            timeZoneMap: TimeZoneMapWrapper
        ): ChildVisit? {
            with(childVisitDataModel) {
                //  If a child visit does not have a valid duration start & end, we simply drop it during conversion
                val locationAppModel = Location.from(locationDataModel = location)
                return if (duration == null || locationAppModel == null) {
                    null
                } else {
                    val eventTimeZone = timeZoneMap.getOverlappingTimeZone(
                        degreesLatitude = locationAppModel.getLatitude(),
                        degreesLongitude = locationAppModel.getLongitude()
                    )
                    ChildVisit(
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
                        eventTimeZone = eventTimeZone
                    )
                }
            }
        }
    }
}

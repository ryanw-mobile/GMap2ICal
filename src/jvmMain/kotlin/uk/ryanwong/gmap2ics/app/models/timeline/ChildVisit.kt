/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.app.models.TimelineItem
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

    fun asTimelineItem(timeZoneMap: TimeZoneMapWrapper, placeDetails: PlaceDetails? = null): TimelineItem {
        val lastEditTimeStamp = lastEditedTimestamp
        val url = placeDetails?.url ?: "https://www.google.com/maps/place/?q=place_id:${location.placeId}"

        return TimelineItem(
            id = lastEditTimeStamp,
            placeId = location.placeId,
            subject = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
            location = placeDetails?.formattedAddress ?: location.address?.replace('\n', ',') ?: "",
            startTimeStamp = durationStartTimestamp,
            endTimeStamp = durationEndTimestamp,
            lastEditTimeStamp = lastEditTimeStamp,
            eventLatLng = placeDetails?.geo ?: LatLng(
                latitude = location.latitudeE7 * 0.0000001,
                longitude = location.longitudeE7 * 0.0000001
            ),
            eventTimeZone = eventTimeZone,
            placeUrl = url,
            description = "Place ID:\\n${location.placeId}\\n\\nGoogle Maps URL:\\n$url"
        )
    }
}
/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

data class PlaceVisit(
    val durationEndTimestamp: String,
    val durationStartTimestamp: String,
    val lastEditedTimestamp: String,
    val location: Location,
//    val locationAssertionType: String? = null,
//    val locationConfidence: Int? = null,
//    val placeConfidence: String? = null,
//    val placeVisitType: String? = null,
//    val visitConfidence: Int? = null,
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

    fun asTimelineItem(placeDetails: PlaceDetails? = null): TimelineItem {
        val url = placeDetails?.url ?: "https://www.google.com/maps/place/?q=place_id:${location.placeId}"
        return TimelineItem(
            id = lastEditedTimestamp,
            placeId = location.placeId,
            subject = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
            location = placeDetails?.formattedAddress ?: location.address?.replace('\n', ',') ?: "",
            startTimeStamp = durationStartTimestamp,
            endTimeStamp = durationEndTimestamp,
            lastEditTimeStamp = lastEditedTimestamp,
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
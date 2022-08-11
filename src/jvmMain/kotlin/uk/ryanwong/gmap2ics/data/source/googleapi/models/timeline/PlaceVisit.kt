/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.app.models.LatLng
import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlaceVisit(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val duration: Duration,
    val lastEditedTimestamp: String? = null,
    val location: Location,
    val locationAssertionType: String? = null,
    val locationConfidence: Int? = null,
    val placeConfidence: String? = null,
    val placeVisitImportance: String? = null,
    val placeVisitType: String? = null,
    val visitConfidence: Int? = null,
    val childVisits: List<ChildVisit>? = null,
) {
    fun asTimelineItem(timeZoneMap: TimeZoneMapWrapper, placeDetails: PlaceDetails? = null): TimelineItem {

        val lastEditTimeStamp = lastEditedTimestamp ?: duration.endTimestamp
        val url = placeDetails?.url ?: "https://www.google.com/maps/place/?q=place_id:${location.placeId}"

        return TimelineItem(
            id = lastEditTimeStamp,
            placeId = location.placeId,
            subject = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
            location = placeDetails?.formattedAddress ?: location.address?.replace('\n', ',') ?: "",
            startTimeStamp = duration.startTimestamp,
            endTimeStamp = duration.endTimestamp,
            lastEditTimeStamp = lastEditTimeStamp,
            eventLatLng = placeDetails?.geo ?: LatLng(
                latitude = location.latitudeE7 * 0.0000001,
                longitude = location.longitudeE7 * 0.0000001
            ),
            eventTimeZone = getEventTimeZone(timeZoneMap),
            placeUrl = url,
            description = "Place ID:\\n${location.placeId}\\n\\nGoogle Maps URL:\\n$url"
        )
    }

    fun getEventTimeZone(timeZoneMap: TimeZoneMapWrapper): TimeZone? {
        val eventLatitude = location.latitudeE7 * 0.0000001
        val eventLongitude = location.longitudeE7 * 0.0000001
        return timeZoneMap.getOverlappingTimeZone(degreesLatitude = eventLatitude, degreesLongitude = eventLongitude)
    }
}
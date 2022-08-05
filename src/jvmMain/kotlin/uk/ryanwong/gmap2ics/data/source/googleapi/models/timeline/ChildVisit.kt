/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.app.models.LatLng
import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.app.models.TimelineItem
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChildVisit(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val duration: Duration? = null,
    val editConfirmationStatus: String? = null,
    val lastEditedTimestamp: String? = null,
    val location: Location,
    val locationAssertionType: String? = null,
    val locationConfidence: Int? = null,
    val placeConfidence: String? = null,
    val placeVisitImportance: String? = null,
    val placeVisitType: String? = null,
    val visitConfidence: Int? = null,
    val placeVisitLevel: Int? = null
) {
    // ChildVisit might have unconfirmed location which does not have a duration
    fun asTimelineItem(timeZoneMap: TimeZoneMap, place: Place? = null): TimelineItem? {
        if (duration == null) {
            return null
        }

        val lastEditTimeStamp = lastEditedTimestamp ?: duration.endTimestamp
        val url = place?.url ?: "https://www.google.com/maps/place/?q=place_id:${location.placeId}"

        return TimelineItem(
            id = lastEditTimeStamp,
            placeId = location.placeId,
            subject = place?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
            location = place?.formattedAddress ?: location.address?.replace('\n', ',') ?: "",
            startTimeStamp = duration.startTimestamp,
            endTimeStamp = duration.endTimestamp,
            lastEditTimeStamp = lastEditTimeStamp,
            eventLatLng = place?.geo ?: LatLng(
                latitude = location.latitudeE7 * 0.0000001,
                longitude = location.longitudeE7 * 0.0000001
            ),
            eventTimeZone = getEventTimeZone(timeZoneMap),
            placeUrl = url,
            description = "Place ID:\\n${location.placeId}\\n\\nGoogle Maps URL:\\n$url"
        )
    }

    fun getEventTimeZone(timeZoneMap: TimeZoneMap): TimeZone? {
        val eventLatitude = location.latitudeE7 * 0.0000001
        val eventLongitude = location.longitudeE7 * 0.0000001
        return timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)
    }
}
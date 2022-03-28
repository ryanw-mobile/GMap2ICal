package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.domain.models.LatLng
import uk.ryanwong.gmap2ics.domain.models.PlaceDetails
import uk.ryanwong.gmap2ics.domain.models.TimelineItem
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlaceVisit(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val duration: Duration,
    val lastEditedTimestamp: String?,
    val location: Location,
    val locationAssertionType: String? = null,
    val locationConfidence: Int? = null,
    val placeConfidence: String? = null,
    val placeVisitImportance: String? = null,
    val placeVisitType: String? = null,
    val simplifiedRawPath: SimplifiedRawPath? = null,
    val visitConfidence: Int? = null,
    val childVisits: List<ChildVisit>? = null,
) {
    fun asTimelineItem(timeZoneMap: TimeZoneMap, placeDetails: PlaceDetails? = null): TimelineItem {

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

    fun getEventTimeZone(timeZoneMap: TimeZoneMap): TimeZone? {
        val eventLatitude = location.latitudeE7 * 0.0000001
        val eventLongitude = location.longitudeE7 * 0.0000001
        return timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)
    }
}
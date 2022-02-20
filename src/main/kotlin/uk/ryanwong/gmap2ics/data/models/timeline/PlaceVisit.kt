package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.domain.models.GMapTimelineObject
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
    fun toGMapTimelineObject(timeZoneMap: TimeZoneMap): GMapTimelineObject {
        val eventLatitude = location.latitudeE7 * 0.0000001
        val eventLongitude = location.longitudeE7 * 0.0000001
        val lastEditTimeStamp = lastEditedTimestamp ?: duration.endTimestamp
        val url = "https://www.google.com/maps/place/?q=place_id:${location.placeId}"

        // TODO: If Location API enabled, try to fetch details from there

        return GMapTimelineObject(
            id = lastEditTimeStamp,
            placeId = location.placeId,
            subject = "\uD83D\uDCCD ${location.name}",
            location = location.address?.replace('\n', ',') ?: "",
            startTimeStamp = duration.startTimestamp,
            endTimeStamp = duration.endTimestamp,
            lastEditTimeStamp = lastEditTimeStamp,
            eventLatitude = eventLatitude,
            eventLongitude = eventLongitude,
            eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude),
            placeUrl = url,
            description = "Place ID:\\n${location.placeId}\\n\\nGoogle Maps URL:\\n$url"
        )
    }
}
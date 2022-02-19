package uk.ryanwong.gmap2ics.domain.models

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class VEvent(
    val uid: String,
    val placeId: String?,
    val dtStamp: String?,
    val organizer: String? = null,
    val dtStart: String,
    val dtEnd: String,
    val dtTimeZone: String,
    val summary: String,
    val location: String,
    val geo: LatLng?,
    val description: String? = null,
    val lastModified: String
) {
    data class LatLng(
        val latitude: Double,
        val longitude: Double
    )

    companion object {
        fun from(timelineObject: GMapTimelineObject): VEvent {
            val timeZone = timelineObject.eventTimeZone

            return VEvent(
                uid = timelineObject.id,
                placeId = timelineObject.placeId,
                dtStamp = timelineObject.lastEditTimeStamp,
                dtStart = getLocalizedTimeStamp(
                    timestamp = timelineObject.startTimeStamp,
                    timezoneId = timelineObject.eventTimeZone?.zoneId ?: "UTC"
                ),
                dtEnd = getLocalizedTimeStamp(
                    timestamp = timelineObject.endTimeStamp,
                    timezoneId = timelineObject.eventTimeZone?.zoneId ?: "UTC"
                ),
                summary = timelineObject.subject,
                geo = LatLng(
                    latitude = timelineObject.eventLatitude,
                    longitude = timelineObject.eventLongitude
                ),
                dtTimeZone = timeZone?.zoneId ?: "UTC",
                location = timelineObject.location,
                lastModified = timelineObject.lastEditTimeStamp
            )
        }
    }

    fun export(): String {
        val stringBuilder = StringBuilder()

        stringBuilder.run {
            append("BEGIN:VEVENT\n")
            append("TRANSP:OPAQUE\n")
            append("DTSTART;TZID=$dtTimeZone:$dtStart\n")
            append("DTEND;TZID=$dtTimeZone:$dtEnd\n")
            append("X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-APPLE-RADIUS=147;\n")
            append("X-TITLE=\"$location\":geo:${geo?.latitude},${geo?.longitude}\n")
            append("UID:$uid\n")
            append("DTSTAMP:$dtStamp\n")
            append("LOCATION:${location.replace(oldValue = ",", newValue = "\\,\\n")}\n")
            append("SUMMARY:$summary\n")
            geo?.let { geo ->
                val googleMapUrl =
                    "https://maps.google.com/?q=${geo.latitude},${geo.longitude}&ll=${geo.latitude},${geo.longitude}&z=14"
                        .replace(oldValue = ",", newValue = "\\,")
                append("DESCRIPTION:$googleMapUrl\n")
            }
//            description?.let { description ->
//                append("DESCRIPTION:$description\n")
//            }
            append("STATUS:CONFIRMED\n")
            append("SEQUENCE:1\n")
            append("LAST-MODIFIED:$lastModified\n") // iso timestamp
            append("CREATED:$lastModified\n") // iso timestamp
            append("X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n")
            append("END:VEVENT\n")
        }

        return stringBuilder.toString()
    }
}

fun getLocalizedTimeStamp(timestamp: String, timezoneId: String): String {
    return DateTimeFormatter
        .ofPattern("yyyyMMdd'T'HHmmss")
        .withZone(ZoneId.of(timezoneId))
        .format(Instant.parse(timestamp))
}

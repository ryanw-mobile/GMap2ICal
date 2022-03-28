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
    val url: String? = null,
    val lastModified: String
) {
    companion object {
        fun from(timelineItem: TimelineItem): VEvent {
            val timeZone = timelineItem.eventTimeZone

            return VEvent(
                uid = timelineItem.id,
                placeId = timelineItem.placeId,
                dtStamp = timelineItem.lastEditTimeStamp,
                dtStart = getLocalizedTimeStamp(
                    timestamp = timelineItem.startTimeStamp,
                    timezoneId = timelineItem.eventTimeZone?.zoneId ?: "UTC"
                ),
                dtEnd = getLocalizedTimeStamp(
                    timestamp = timelineItem.endTimeStamp,
                    timezoneId = timelineItem.eventTimeZone?.zoneId ?: "UTC"
                ),
                summary = timelineItem.subject,
                geo = timelineItem.eventLatLng,
                dtTimeZone = timeZone?.zoneId ?: "UTC",
                location = timelineItem.location,
                url = timelineItem.placeUrl,
                lastModified = timelineItem.lastEditTimeStamp,
                description = timelineItem.description
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
            val xTitle = location.ifBlank { geo?.getFormattedLatLng() ?: "0,0" }
            append(
                "X-TITLE=\"${
                    xTitle.replace(oldValue = "\n", newValue = "\\, ")
                }\":geo:${geo?.getFormattedLatLng() ?: "0,0"}\n"
            )
            append("UID:$uid\n")
            append("DTSTAMP:$dtStamp\n")
            append(
                "LOCATION:${
                    location
                        .replace(oldValue = "\n", newValue = ", ")
                        .replace(oldValue = ",", newValue = "\\,\\n")
                }\n"
            )
            append("SUMMARY:$summary\n")
//            geo?.let { geo ->
//                val googleMapUrl =
//                    "https://maps.google.com/?q=${geo.latitude},${geo.longitude}&ll=${geo.latitude},${geo.longitude}&z=14"
//                        .replace(oldValue = ",", newValue = "\\,")
//                append("DESCRIPTION:$googleMapUrl\n")
//            }
            description?.let { description ->
                append("DESCRIPTION:$description\n")
            }
            url?.let { url ->
                append("URL;VALUE=URI:$url\n")
            }
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

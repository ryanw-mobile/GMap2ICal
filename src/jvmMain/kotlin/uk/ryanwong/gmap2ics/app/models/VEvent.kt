/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import us.dustinj.timezonemap.TimeZone
import java.text.DecimalFormat
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
        private val mileageFormat = DecimalFormat("#,###.#")
        fun from(
            activitySegment: ActivitySegment,
            shouldShowMiles: Boolean,
            firstPlaceDetails: PlaceDetails?,
            lastPlaceDetails: PlaceDetails?,
            startPlaceDetails: PlaceDetails?,
            endPlaceDetails: PlaceDetails?,
            eventTimeZone: TimeZone?
        ): VEvent {
            with(activitySegment) {
                val distanceInKilometers: Double = distance / 1000.0
                val distanceString = if (shouldShowMiles)
                    "${mileageFormat.format(ActivitySegmentFormatter.kilometersToMiles(distanceInKilometers))}mi"
                else
                    "${mileageFormat.format(distanceInKilometers)}km"

                val subject = "${activityType.emoji} $distanceString ${
                    ActivitySegmentFormatter.parseActivityRouteText(
                        startPlaceDetails = startPlaceDetails,
                        endPlaceDetails = endPlaceDetails,
                        startLocation = startLocation.name,
                        endLocation = endLocation.name
                    )
                }"

                // Try to extract more meaningful information than just the miles travelled
                val startLocationText = ActivitySegmentFormatter.getStartLocationText(
                    startLocation = startLocation,
                    placeDetails = startPlaceDetails
                )
                val endLocationText =
                    ActivitySegmentFormatter.getEndLocationText(
                        endLocation = endLocation,
                        placeDetails = endPlaceDetails
                    )

                val description = ActivitySegmentFormatter.parseTimelineDescription(
                    startLocationText = startLocationText,
                    endLocationText = endLocationText,
                    startPlaceDetails = firstPlaceDetails,
                    endPlaceDetails = lastPlaceDetails
                )

                val timeZoneId = eventTimeZone?.zoneId ?: "UTC"

                return VEvent(
                    uid = lastEditedTimestamp,
                    placeId = endLocation.placeId, // Usually null
                    dtStamp = lastEditedTimestamp,
                    dtStart = getLocalizedTimeStamp(
                        timestamp = durationStartTimestamp,
                        timezoneId = timeZoneId
                    ),
                    dtEnd = getLocalizedTimeStamp(
                        timestamp = durationEndTimestamp,
                        timezoneId = timeZoneId
                    ),
                    summary = subject,
                    geo = LatLng(
                        latitude = endLocation.getLatitude(),
                        longitude = endLocation.getLongitude()
                    ),
                    dtTimeZone = timeZoneId,
                    location = endLocation.address ?: lastPlaceDetails?.formattedAddress
                    ?: endLocation.getFormattedLatLng(),
                    url = endLocation.placeId?.let { endLocation.getGoogleMapsPlaceIdLink() }
                        ?: endLocation.getGoogleMapsLatLngLink(),
                    lastModified = lastEditedTimestamp,
                    description = description
                )
            }
        }

        fun from(placeVisit: PlaceVisit, placeDetails: PlaceDetails? = null): VEvent {
            with(placeVisit) {
                val url = placeDetails?.url ?: "https://www.google.com/maps/place/?q=place_id:${location.placeId}"
                val timeZoneId = eventTimeZone?.zoneId ?: "UTC"

                return VEvent(
                    uid = lastEditedTimestamp,
                    placeId = location.placeId,
                    dtStamp = lastEditedTimestamp,
                    dtStart = getLocalizedTimeStamp(
                        timestamp = durationStartTimestamp,
                        timezoneId = timeZoneId
                    ),
                    dtEnd = getLocalizedTimeStamp(
                        timestamp = durationEndTimestamp,
                        timezoneId = timeZoneId
                    ),
                    summary = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
                    geo = placeDetails?.geo ?: LatLng(
                        latitude = location.latitudeE7 * 0.0000001,
                        longitude = location.longitudeE7 * 0.0000001
                    ),
                    dtTimeZone = timeZoneId,
                    location = placeDetails?.formattedAddress ?: location.address?.replace('\n', ',') ?: "",
                    url = url,
                    lastModified = lastEditedTimestamp,
                    description = "Place ID:\\n${location.placeId}\\n\\nGoogle Maps URL:\\n$url"
                )
            }
        }

        fun from(childVisit: ChildVisit, placeDetails: PlaceDetails? = null): VEvent {
            with(childVisit) {
                val url = placeDetails?.url ?: "https://www.google.com/maps/place/?q=place_id:${location.placeId}"
                val timeZoneId = eventTimeZone?.zoneId ?: "UTC"

                return VEvent(
                    uid = lastEditedTimestamp,
                    placeId = location.placeId,
                    dtStamp = lastEditedTimestamp,
                    dtStart = getLocalizedTimeStamp(
                        timestamp = durationStartTimestamp,
                        timezoneId = timeZoneId
                    ),
                    dtEnd = getLocalizedTimeStamp(
                        timestamp = durationEndTimestamp,
                        timezoneId = timeZoneId
                    ),
                    summary = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
                    geo = placeDetails?.geo ?: LatLng(
                        latitude = location.latitudeE7 * 0.0000001,
                        longitude = location.longitudeE7 * 0.0000001
                    ),
                    dtTimeZone = timeZoneId,
                    location = placeDetails?.formattedAddress ?: location.address?.replace('\n', ',') ?: "",
                    url = url,
                    lastModified = lastEditedTimestamp,
                    description = "Place ID:\\n${location.placeId}\\n\\nGoogle Maps URL:\\n$url"
                )
            }
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
                // X-Title string has not much value. keep that simple.
                "X-TITLE=\"${
                    xTitle.replace(oldValue = "\n", newValue = " ")
                        .replace(oldValue = ",", newValue = " ")
                }\":geo:${geo?.getFormattedLatLng() ?: "0,0"}\n"
            )
            append("UID:$uid\n")
            append("DTSTAMP:$dtStamp\n")
            append(
                "LOCATION:${
                    location
                        .replace(oldValue = "\n", newValue = ", ")
                        .replace(oldValue = ",", newValue = "\\,")
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
                append("URL;VALUE=URI:${url.replace(oldValue = ",", newValue = "\\,")}\n")
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

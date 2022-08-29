/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import java.text.DecimalFormat

/***
 * Design note:
 * Reasons for having other App Models, but not making VEvent to take data models as input directly,
 * is that VEvent is the final, stringified output which lose most of the semantic meaning that the original
 * ActivitySegment, Place visits can provide. To process this in one single step can make things more difficult
 * to understand, thus harder to debug if something goes wrong, or when Google Maps change the data they return.
 *
 * Adding App Models between Data Models and VEvent gives us some room to carefully polish the data for each data
 * type, before handing them to VEvent for export purpose.
 */

data class VEvent(
    val uid: String,
    val placeId: String?,
    val dtStamp: String?,
    val organizer: String? = null,
    val dtStart: RawTimestamp,
    val dtEnd: RawTimestamp,
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
            endPlaceDetails: PlaceDetails?
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

                return VEvent(
                    uid = lastEditedTimestamp,
                    placeId = endLocation.placeId, // Usually null
                    dtStamp = lastEditedTimestamp,
                    dtStart = durationStartTimestamp,
                    dtEnd = durationEndTimestamp,
                    summary = subject,
                    geo = LatLng(
                        latitude = endLocation.getLatitude(),
                        longitude = endLocation.getLongitude()
                    ),
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

                return VEvent(
                    uid = lastEditedTimestamp,
                    placeId = location.placeId,
                    dtStamp = lastEditedTimestamp,
                    dtStart = durationStartTimestamp,
                    dtEnd = durationEndTimestamp,
                    summary = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
                    geo = placeDetails?.geo ?: LatLng(
                        latitude = location.latitudeE7 * 0.0000001,
                        longitude = location.longitudeE7 * 0.0000001
                    ),
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

                return VEvent(
                    uid = lastEditedTimestamp,
                    placeId = location.placeId,
                    dtStamp = lastEditedTimestamp,
                    dtStart = durationStartTimestamp,
                    dtEnd = durationEndTimestamp,
                    summary = placeDetails?.getFormattedName() ?: "\uD83D\uDCCD ${location.name}",
                    geo = placeDetails?.geo ?: LatLng(
                        latitude = location.latitudeE7 * 0.0000001,
                        longitude = location.longitudeE7 * 0.0000001
                    ),
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
            append("DTSTART;TZID=${dtStart.timezoneId}:${dtStart.toLocalizedTimestamp()}\n")
            append("DTEND;TZID=${dtEnd.timezoneId}:${dtEnd.toLocalizedTimestamp()}\n")
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

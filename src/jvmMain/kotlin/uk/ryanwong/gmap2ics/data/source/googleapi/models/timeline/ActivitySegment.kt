/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.LatLng
import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone
import java.text.DecimalFormat

@JsonIgnoreProperties(ignoreUnknown = true)
data class ActivitySegment(
    val activities: List<Activity>? = null,
    val activityType: String? = null,
    val confidence: String? = null,
    val distance: Int? = null,
    val duration: Duration,
    val endLocation: ActivityLocation,
    val startLocation: ActivityLocation,
    val waypointPath: WaypointPath? = null,
    val lastEditedTimestamp: String? = null,
    val activityConfidence: Int? = null
) {
    private val mileageFormat = DecimalFormat("#,###.#")

    fun asTimelineItem(
        shouldShowMiles: Boolean,
        firstPlaceDetails: PlaceDetails?,
        lastPlaceDetails: PlaceDetails?,
        startPlaceDetails: PlaceDetails?,
        endPlaceDetails: PlaceDetails?,
        eventTimeZone: TimeZone?
    ): TimelineItem {
        val eventLatitude = (endLocation.latitudeE7 ?: 0) * 0.0000001
        val eventLongitude = (endLocation.longitudeE7 ?: 0) * 0.0000001
        val lastEditTimeStamp = lastEditedTimestamp ?: duration.endTimestamp

        val distanceInKilometers: Double? = distance?.let { distance ->
            distance / 1000.0
        } ?: waypointPath?.distanceMeters?.let { distanceMeters ->
            distanceMeters / 1000.0
        }

        val distanceString = distanceInKilometers?.let { kilometers ->
            if (shouldShowMiles)
                "${mileageFormat.format(kilometersToMiles(kilometers))}mi"
            else
                "${mileageFormat.format(kilometers)}km"
        } ?: ""

        val activityType = activityType?.let {
            try {
                ActivityType.valueOf(activityType)
            } catch (e: IllegalArgumentException) {
                println("⚠️  Activity $activityType unknown, mapping as UNKNOWN_ACTIVITY_TYPE")
                ActivityType.UNKNOWN_ACTIVITY_TYPE
            }
        } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

        val subject = "${activityType.emoji} $distanceString ${
            parseActivityRouteText(
                startPlaceDetails = startPlaceDetails,
                endPlaceDetails = endPlaceDetails,
                startLocation = startLocation.name,
                endLocation = endLocation.name
            )
        }"

        // Try to extract more meaningful information than just the miles travelled
        val startLocationText = getStartLocationText(placeDetails = startPlaceDetails)
        val endLocationText = getEndLocationText(placeDetails = endPlaceDetails)

        val description = parseTimelineDescription(
            startLocationText = startLocationText,
            endLocationText = endLocationText,
            startPlaceDetails = firstPlaceDetails,
            endPlaceDetails = lastPlaceDetails
        )

        return TimelineItem(
            id = lastEditTimeStamp,
            placeId = endLocation.placeId, // Usually null
            subject = subject,
            location = endLocation.address ?: lastPlaceDetails?.formattedAddress ?: endLocation.getFormattedLatLng()
            ?: "Unknown",
            startTimeStamp = duration.startTimestamp,
            endTimeStamp = duration.endTimestamp,
            lastEditTimeStamp = lastEditTimeStamp,
            eventLatLng = LatLng(
                latitude = eventLatitude,
                longitude = eventLongitude
            ),
            eventTimeZone = eventTimeZone,
            placeUrl = endLocation.placeId?.let { endLocation.getGoogleMapsPlaceIdLink() }
                ?: endLocation.getGoogleMapsLatLngLink(),
            description = description
        )
    }

    private fun parseTimelineDescription(
        startPlaceDetails: PlaceDetails?,
        endPlaceDetails: PlaceDetails?,
        startLocationText: String,
        endLocationText: String
    ): String {
        // Segments are less accurate than start and end locations,
        // but still have some values if the start and end locations do not have a valid placeId
        val firstSegmentText = startPlaceDetails?.let { placeDetails ->
            "First segment: ${placeDetails.formattedAddress}\\nhttps://www.google.com/maps/place/?q=place_id:${placeDetails.placeId}\\n\\n"
        } ?: ""

        val lastSegmentText = endPlaceDetails?.let { placeDetails ->
            "Last segment: ${placeDetails.formattedAddress}\\nhttps://www.google.com/maps/place/?q=place_id:${placeDetails.placeId}\\n\\n"
        } ?: ""

        return startLocationText +
                endLocationText +
                firstSegmentText +
                lastSegmentText
    }

    private fun getStartLocationText(placeDetails: PlaceDetails?): String {
        return placeDetails?.let { place ->
            "Start Location: ${place.formattedAddress}\\n${startLocation.getGoogleMapsPlaceIdLink()}\\n\\n"
        }
            ?: "Start Location: ${startLocation.getFormattedLatLng()}\\n${startLocation.getGoogleMapsLatLngLink()}\\n\\n"

    }

    private fun getEndLocationText(placeDetails: PlaceDetails?): String {
        return placeDetails?.let { place ->
            "End Location: ${place.formattedAddress}\\n${endLocation.getGoogleMapsPlaceIdLink()}\\n\\n"
        }
            ?: "End Location: ${endLocation.getFormattedLatLng()}\\n${endLocation.getGoogleMapsLatLngLink()}\\n\\n"
    }

    private fun parseActivityRouteText(
        startPlaceDetails: PlaceDetails?,
        endPlaceDetails: PlaceDetails?,
        startLocation: String?,
        endLocation: String?
    ): String {
        // PlaceDetails are the most reliable source
        if (startPlaceDetails != null || endPlaceDetails != null) {
            return "(${startPlaceDetails?.name} ➡ ${endPlaceDetails?.name})"
        }

        return if (startLocation == null && endLocation == null) ""
        else "(${startLocation} ➡ ${endLocation})"
    }

    private fun kilometersToMiles(meters: Double): Double = meters * 0.621

    fun getEventTimeZone(timeZoneMap: TimeZoneMapWrapper): TimeZone? {
        val eventLatitude = endLocation.getLatitude() ?: 0.0
        val eventLongitude = endLocation.getLongitude() ?: 0.0
        return timeZoneMap.getOverlappingTimeZone(degreesLatitude = eventLatitude, degreesLongitude = eventLongitude)
    }

//    private fun getCurrentIsoTimestamp() = DateTimeFormatter
//        .ofPattern("yyyyMMddTHHmmssZ")
//        .withZone(ZoneOffset.UTC)
//        .format(Instant.now())
}
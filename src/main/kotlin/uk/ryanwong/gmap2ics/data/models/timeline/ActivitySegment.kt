package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.getLabel
import uk.ryanwong.gmap2ics.domain.models.TimelineItem
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@JsonIgnoreProperties(ignoreUnknown = true)
data class ActivitySegment(
    val activities: List<Activity>? = null,
    val activityType: String? = null,
    val confidence: String? = null,
    val distance: Int? = null,
    val duration: Duration,
    val endLocation: ActivityLocation,
    // val parkingEvent: ParkingEvent? = null,
    val simplifiedRawPath: SimplifiedRawPath? = null,
    val startLocation: ActivityLocation,
    val waypointPath: WaypointPath? = null,
    val lastEditedTimestamp: String? = null,
    val activityConfidence: Int? = null
) {
    private val mileageFormat = DecimalFormat("#,###.#")

    fun asTimelineItem(timeZoneMap: TimeZoneMap): TimelineItem {
        val eventLatitude = (endLocation.latitudeE7 ?: 0) * 0.0000001
        val eventLongitude = (endLocation.longitudeE7 ?: 0) * 0.0000001
        val eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)
        val lastEditTimeStamp = lastEditedTimestamp ?: duration.endTimestamp

        val distanceInKilometers: Double? = distance?.let { distance ->
            distance / 1000.0
        } ?: waypointPath?.distanceMeters?.let { distanceMeters ->
            distanceMeters / 1000.0
        }

        val distanceString = distanceInKilometers?.let { kilometers ->
            if (shouldShowMiles(eventTimeZone))
                "${mileageFormat.format(kilometersToMiles(kilometers))}mi"
            else
                "${mileageFormat.format(kilometers)}km"
        } ?: ""

        val activityType = activityType?.let {
            try {
                ActivityType.valueOf(activityType)
            } catch (e: IllegalArgumentException) {
                println("⚠️  Activity ${activityType} unknown, mapping as UNKNOWN_ACTIVITY_TYPE")
                ActivityType.UNKNOWN_ACTIVITY_TYPE
            }
        } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

        // Generate emoji label prefix for different activity types
        val activityLabel = activityType.getLabel()

        val subject = "$activityLabel $distanceString ${
            parseActivityRouteText(
                startLocation.name,
                endLocation.name
            )
        }"


        // TODO: If Location API enabled, try to fetch starting and ending from there
        // However chances are we have cached the starting point - also we need to cache the destination

        return TimelineItem(
            id = lastEditTimeStamp,
            placeId = endLocation.placeId, // Usually null
            subject = subject,
            location = endLocation.address ?: "",
            startTimeStamp = duration.startTimestamp,
            endTimeStamp = duration.endTimestamp,
            lastEditTimeStamp = lastEditTimeStamp,
            eventLatitude = eventLatitude,
            eventLongitude = eventLongitude,
            eventTimeZone = eventTimeZone
        )
    }

    private fun parseActivityRouteText(
        startLocation: String?,
        endLocation: String?
    ): String {
        if (startLocation == null && endLocation == null) return ""

        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        startLocation?.let { location ->
            stringBuilder.append(location)
        }
        endLocation?.let { location ->
            stringBuilder.append("➡ $location")
        }
        stringBuilder.append(")")

        return stringBuilder.toString()
    }

    private fun kilometersToMiles(meters: Double): Double = meters * 0.621

    private fun shouldShowMiles(timezone: TimeZone?): Boolean {
        return timezone?.zoneId == "Europe/London"
    }

    private fun getCurrentIsoTimestamp() = DateTimeFormatter
        .ofPattern("yyyyMMddTHHmmssZ")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
}
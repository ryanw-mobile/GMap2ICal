package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.models.timeline.ChildVisit
import uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisit
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.getLabel
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val mileageFormat = DecimalFormat("#,###.#")
private val timeZoneMap = TimeZoneMap.forEverywhere()

data class GMapTimelineObject(
    val id: String,
    val subject: String,
    val location: String,
    val startTimeStamp: String,
    val endTimeStamp: String,
    val lastEditTimeStamp: String,
    val eventLatitude: Double,
    val eventLongitude: Double,
    val eventTimeZone: TimeZone?
) {

    companion object {
        fun from(placeVisit: PlaceVisit): GMapTimelineObject {
            val eventLatitude = placeVisit.location.latitudeE7 * 0.0000001
            val eventLongitude = placeVisit.location.longitudeE7 * 0.0000001
            val lastEditTimeStamp = getLastEditTimeStamp(
                startTimeStamp = placeVisit.duration.startTimestamp,
                endTimeStamp = placeVisit.duration.endTimestamp,
                lastEditTimeStamp = placeVisit.lastEditedTimestamp
            )

            // TODO: If Location API enabled, try to fetch details from there

            return GMapTimelineObject(
                id = lastEditTimeStamp,
                subject = "\uD83D\uDCCD ${placeVisit.location.name}",
                location = placeVisit.location.address?.replace('\n', ',') ?: "",
                startTimeStamp = placeVisit.duration.startTimestamp,
                endTimeStamp = placeVisit.duration.endTimestamp,
                lastEditTimeStamp = lastEditTimeStamp,
                eventLatitude = eventLatitude,
                eventLongitude = eventLongitude,
                eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)
            )
        }

        fun from(childVisit: ChildVisit): GMapTimelineObject {
            val eventLatitude = childVisit.location.latitudeE7 * 0.0000001
            val eventLongitude = childVisit.location.longitudeE7 * 0.0000001
            val lastEditTimeStamp = getLastEditTimeStamp(
                startTimeStamp = childVisit.duration.startTimestamp,
                endTimeStamp = childVisit.duration.endTimestamp,
                lastEditTimeStamp = childVisit.lastEditedTimestamp
            )

            // TODO: If Location API enabled, try to fetch details from there

            return GMapTimelineObject(
                id = lastEditTimeStamp,
                subject = "\uD83D\uDCCD ${childVisit.location.name}",
                location = childVisit.location.address?.replace('\n', ',') ?: "",
                startTimeStamp = childVisit.duration.startTimestamp,
                endTimeStamp = childVisit.duration.endTimestamp,
                lastEditTimeStamp = lastEditTimeStamp,
                eventLatitude = eventLatitude,
                eventLongitude = eventLongitude,
                eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)
            )
        }

        fun from(activitySegment: ActivitySegment): GMapTimelineObject {
            val eventLatitude = (activitySegment.endLocation?.latitudeE7 ?: 0) * 0.0000001
            val eventLongitude = (activitySegment.endLocation?.longitudeE7 ?: 0) * 0.0000001
            val eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)

            val distanceInKilometers: Double? = activitySegment.distance?.let { distance ->
                distance / 1000.0
            } ?: activitySegment.waypointPath?.distanceMeters?.let { distanceMeters ->
                distanceMeters / 1000.0
            }

            val distanceString = distanceInKilometers?.let { kilometers ->
                if (shouldShowMiles(eventTimeZone))
                    "${mileageFormat.format(kilometersToMiles(kilometers))}mi"
                else
                    "${mileageFormat.format(kilometers)}km"
            } ?: ""

            val activityType = activitySegment.activityType?.let {
                try {
                    ActivityType.valueOf(activitySegment.activityType)
                } catch (e: IllegalArgumentException) {
                    println("⚠️  ${activitySegment.activityType}")
                    ActivityType.UNKNOWN_ACTIVITY_TYPE
                }
            } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

            // Generate emoji label prefix for different activity types
            val activityLabel = when (activityType) {
                ActivityType.UNKNOWN_ACTIVITY_TYPE -> "❓"
                ActivityType.BOATING -> "🚣‍♀️"
                ActivityType.CYCLING -> "🚲"
                ActivityType.FLYING -> "✈️"
                ActivityType.HIKING -> "⛰"
                ActivityType.IN_BUS -> "🚎"
                ActivityType.IN_CABLECAR -> "🚠"
                ActivityType.IN_FERRY -> "⛴"
                ActivityType.IN_PASSENGER_VEHICLE -> "\uD83D\uDE97"
                ActivityType.IN_SUBWAY -> "🚇"
                ActivityType.IN_TAXI -> "🚖"
                ActivityType.IN_TRAIN -> "🚆"
                ActivityType.IN_TRAM -> "🚋"
                ActivityType.IN_VEHICLE -> "🚐"
                ActivityType.MOTORCYCLING -> "🏍"
                ActivityType.RUNNING -> "🏃‍♀️"
                ActivityType.SAILING -> "⛵️"
                ActivityType.SKIING -> "⛷"
                ActivityType.STILL -> "\uD83E\uDDCD"
                ActivityType.WALKING -> "🚶‍♂️"
            }

            val subject = "$activityLabel $distanceString ${
                parseActivityRouteText(
                    activitySegment.startLocation?.name,
                    activitySegment.endLocation?.name
                )
            }"

            val lastEditTimeStamp = getLastEditTimeStamp(
                startTimeStamp = activitySegment.duration.startTimestamp,
                endTimeStamp = activitySegment.duration.endTimestamp,
                lastEditTimeStamp = activitySegment.lastEditedTimestamp
            )

            // TODO: If Location API enabled, try to fetch starting and ending from there
            // However chances are we have cached the starting point - also we need to cache the destination

            return GMapTimelineObject(
                id = lastEditTimeStamp,
                subject = subject,
                location = activitySegment.endLocation?.address ?: "",
                startTimeStamp = activitySegment.duration.startTimestamp,
                endTimeStamp = activitySegment.duration.endTimestamp,
                lastEditTimeStamp = lastEditTimeStamp,
                eventLatitude = eventLatitude,
                eventLongitude = eventLongitude,
                eventTimeZone = eventTimeZone
            )
        }
    }
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

private fun getLastEditTimeStamp(
    startTimeStamp: String?,
    endTimeStamp: String?,
    lastEditTimeStamp: String?
): String {
    lastEditTimeStamp?.let { return it }
    endTimeStamp?.let { return it }
    return startTimeStamp ?: getCurrentIsoTimestamp()
}

private fun kilometersToMiles(meters: Double): Double = meters * 0.621

private fun shouldShowMiles(timezone: TimeZone?): Boolean {
    return timezone?.zoneId == "Europe/London"
}

private fun getCurrentIsoTimestamp() = DateTimeFormatter
    .ofPattern("yyyyMMddTHHmmssZ")
    .withZone(ZoneOffset.UTC)
    .format(Instant.now())

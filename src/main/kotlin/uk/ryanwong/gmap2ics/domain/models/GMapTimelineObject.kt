package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.data.models.ActivitySegment
import uk.ryanwong.gmap2ics.data.models.PlaceVisit
import uk.ryanwong.gmap2ics.domain.ActivityType
import us.dustinj.timezonemap.TimeZoneMap
import java.text.DecimalFormat

private val mileageFormat = DecimalFormat("#,###.#")
private val timeZoneMap = TimeZoneMap.forEverywhere()

data class GMapTimelineObject(
    val id: String,
    val subject: String,
    val location: String,
    val startTimeStamp: String?,
    val endTimeStamp: String?,
    val lastEditTimeStamp: String,
    val eventLatitude: Double,
    val eventLongitude: Double,
    val eventTimeZone: String
) {

    companion object {
        fun from(placeVisit: PlaceVisit): GMapTimelineObject {
            val eventLatitude = (placeVisit.location?.latitudeE7 ?: 0) * 0.0000001
            val eventLongitude = (placeVisit.location?.longitudeE7 ?: 0) * 0.0000001

            return GMapTimelineObject(
                id = getLastEditTimeStamp(
                    startTimeStamp = placeVisit.duration?.startTimestamp,
                    endTimeStamp = placeVisit.duration?.endTimestamp,
                    lastEditTimeStamp = placeVisit.lastEditedTimestamp
                ), //        println(LocalDateTime.now().atZone(ZoneId.of("UTC")))
                subject = "\uD83D\uDCCD ${placeVisit.location?.name}",
                location = placeVisit.location?.address?.replace('\n', ',') ?: "",
                startTimeStamp = placeVisit.duration?.startTimestamp,
                endTimeStamp = placeVisit.duration?.endTimestamp,
                lastEditTimeStamp = getLastEditTimeStamp(
                    startTimeStamp = placeVisit.duration?.startTimestamp,
                    endTimeStamp = placeVisit.duration?.endTimestamp,
                    lastEditTimeStamp = placeVisit.lastEditedTimestamp
                ),
                eventLatitude = eventLatitude,
                eventLongitude = eventLongitude,
                eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)?.zoneId ?: ""
            )
        }

        fun from(activitySegment: ActivitySegment): GMapTimelineObject {
            val eventLatitude = (activitySegment.endLocation?.latitudeE7 ?: 0) * 0.0000001
            val eventLongitude = (activitySegment.endLocation?.longitudeE7 ?: 0) * 0.0000001
            val eventTimeZone = timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)?.zoneId ?: ""

            val distanceInKilometers: Double? = activitySegment.distance?.let { distance ->
                distance / 1000.0
            } ?: activitySegment.waypointPath?.distanceMeters?.let { distanceMeters ->
                distanceMeters / 1000.0
            }

            val distanceString = distanceInKilometers?.let { kilometers ->
                if ("Europe/London" == eventTimeZone)
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

            val subject = when (activityType) {
//                ActivityType.WALKING -> {}
                ActivityType.IN_PASSENGER_VEHICLE ->
                    "\uD83D\uDE97 $distanceString ${parseActivityRouteText(activitySegment.startLocation?.name, activitySegment.endLocation?.name)}"

                ActivityType.UNKNOWN_ACTIVITY_TYPE ->
                    "❓ $distanceString ${parseActivityRouteText(activitySegment.startLocation?.name, activitySegment.endLocation?.name)}"

                else ->
                    "\uD83D\uDC40 ${activitySegment.activityType} for $distanceString ${parseActivityRouteText(activitySegment.startLocation?.name, activitySegment.endLocation?.name)}"
            }

            return GMapTimelineObject(
                id = getLastEditTimeStamp(
                    startTimeStamp = activitySegment.duration?.startTimestamp,
                    endTimeStamp = activitySegment.duration?.endTimestamp,
                    lastEditTimeStamp = activitySegment.lastEditedTimestamp
                ), //        println(LocalDateTime.now().atZone(ZoneId.of("UTC")))
                subject = subject,
                location = activitySegment.endLocation?.address ?: "",
                startTimeStamp = activitySegment.duration?.startTimestamp,
                endTimeStamp = activitySegment.duration?.endTimestamp,
                lastEditTimeStamp = getLastEditTimeStamp(
                    startTimeStamp = activitySegment.duration?.startTimestamp,
                    endTimeStamp = activitySegment.duration?.endTimestamp,
                    lastEditTimeStamp = activitySegment.lastEditedTimestamp
                ),
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
) : String {
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
    return startTimeStamp ?: ""
}

fun kilometersToMiles(meters: Double): Double = meters * 0.621

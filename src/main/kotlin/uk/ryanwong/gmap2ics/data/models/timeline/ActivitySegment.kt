package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.getLabel
import uk.ryanwong.gmap2ics.domain.models.LatLng
import uk.ryanwong.gmap2ics.domain.models.PlaceDetails
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
    val startLocation: ActivityLocation,
    // val roadSegment: List<RoadSegment>? = null,
    // val simplifiedRawPath: SimplifiedRawPath? = null,
    val waypointPath: WaypointPath? = null,
    val lastEditedTimestamp: String? = null,
    val activityConfidence: Int? = null
) {
    private val mileageFormat = DecimalFormat("#,###.#")

    suspend fun asTimelineItem(timeZoneMap: TimeZoneMap, placeDetailsRepository: PlaceDetailsRepository): TimelineItem {
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
                println("⚠️  Activity $activityType unknown, mapping as UNKNOWN_ACTIVITY_TYPE")
                ActivityType.UNKNOWN_ACTIVITY_TYPE
            }
        } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

        // Generate emoji label prefix for different activity types
        val activityLabel = activityType.getLabel()

        // If Location API enabled, try to fetch starting and ending from there
        val startPlaceDetail = startLocation.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = getEventTimeZone(timeZoneMap = timeZoneMap)?.zoneId
            )
        }
        val endPlaceDetail = endLocation.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = getEventTimeZone(timeZoneMap = timeZoneMap)?.zoneId
            )
        }

        val subject = "$activityLabel $distanceString ${
            parseActivityRouteText(
                startPlaceDetail = startPlaceDetail,
                endPlaceDetail = endPlaceDetail,
                startLocation = startLocation.name,
                endLocation = endLocation.name
            )
        }"

        val description = parseTimelineDescription(
            startPlaceDetail = startPlaceDetail,
            endPlaceDetail = endPlaceDetail,
            placeDetailsRepository = placeDetailsRepository,
            timeZoneMap = timeZoneMap
        )

        return TimelineItem(
            id = lastEditTimeStamp,
            placeId = endLocation.placeId, // Usually null
            subject = subject,
            location = endLocation.address ?: "",
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

    private suspend fun parseTimelineDescription(
        startPlaceDetail: PlaceDetails?,
        endPlaceDetail: PlaceDetails?,
        placeDetailsRepository: PlaceDetailsRepository,
        timeZoneMap: TimeZoneMap
    ): String {
        // Try to extract more meaningful information than just the miles travelled
        val startLocationText = getStartLocationText(placeDetail = startPlaceDetail)
        val endLocationText = getEndLocationText(placeDetail = endPlaceDetail)

        // Segments are less accurate than start and end locations,
        // but still have some values if the start and end locations do not have a valid placeId
        val firstSegmentText = waypointPath?.roadSegment?.first()?.placeId?.let { placeId ->
            val placeDetail = placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = getEventTimeZone(timeZoneMap = timeZoneMap)?.zoneId
            )
            "First segment: ${placeDetail?.formattedAddress}\\nhttps://www.google.com/maps/place/?q=place_id:${placeId}\\n\\n"
        } ?: ""

        val lastSegmentText = waypointPath?.roadSegment?.last()?.placeId?.let { placeId ->
            val placeDetail = placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = getEventTimeZone(timeZoneMap = timeZoneMap)?.zoneId
            )
            "Last segment: ${placeDetail?.formattedAddress}\\nhttps://www.google.com/maps/place/?q=place_id:${placeId}\\n\\n"
        } ?: ""

        return startLocationText +
                endLocationText +
                firstSegmentText +
                lastSegmentText
    }

    private fun getStartLocationText(placeDetail: PlaceDetails?): String {
        return placeDetail?.let {
            "Start Location: ${placeDetail.formattedAddress}\\n${startLocation.getGoogleMapsPlaceIdLink()}\\n\\n"
        }
            ?: "Start Location: ${startLocation.getFormattedLatLng()}\\n${startLocation.getGoogleMapsLatLngLink()}\\n\\n"

    }

    private fun getEndLocationText(placeDetail: PlaceDetails?): String {
        return placeDetail?.let {
            "End Location: ${placeDetail.formattedAddress}\\n${endLocation.getGoogleMapsPlaceIdLink()}\\n\\n"
        }
            ?: "End Location: ${endLocation.getFormattedLatLng()}\\n${endLocation.getGoogleMapsLatLngLink()}\\n\\n"
    }

    private fun parseActivityRouteText(
        startPlaceDetail: PlaceDetails?,
        endPlaceDetail: PlaceDetails?,
        startLocation: String?,
        endLocation: String?
    ): String {
        // PlaceDetails are the most reliable source
        if (startPlaceDetail != null || endPlaceDetail != null) {
            return "(${startPlaceDetail?.name} ➡ ${endPlaceDetail?.name})"
        }

        return if (startLocation == null && endLocation == null) ""
        else "(${startLocation} ➡ ${endLocation})"
    }

    private fun kilometersToMiles(meters: Double): Double = meters * 0.621

    private fun shouldShowMiles(timezone: TimeZone?): Boolean {
        return timezone?.zoneId == "Europe/London"
    }

    private fun getEventTimeZone(timeZoneMap: TimeZoneMap): TimeZone? {
        val eventLatitude = endLocation.getLatitude() ?: 0.0
        val eventLongitude = endLocation.getLongitude() ?: 0.0
        return timeZoneMap.getOverlappingTimeZone(eventLatitude, eventLongitude)
    }

    private fun getCurrentIsoTimestamp() = DateTimeFormatter
        .ofPattern("yyyyMMddTHHmmssZ")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
}
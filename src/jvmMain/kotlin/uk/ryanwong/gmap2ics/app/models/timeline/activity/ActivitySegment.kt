/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone
import java.text.DecimalFormat

data class ActivitySegment(
    val activities: List<Activity>,
    val activityType: ActivityType,
    val rawActivityType: String?, // for debug use when resolved into an unknown type
    val distance: Int,
    val durationEndTimestamp: String,
    val durationStartTimestamp: String,
    val endLocation: Location,
    val startLocation: Location,
    val waypointPath: WaypointPath? = null,
    val lastEditedTimestamp: String,
    val eventTimeZone: TimeZone?
) {
    companion object {
        fun from(
            activitySegmentDataModel: uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment,
            timeZoneMap: TimeZoneMapWrapper
        ): ActivitySegment? {
            with(activitySegmentDataModel) {
                val startLocationAppModel: Location? = Location.from(activityLocationDataModel = startLocation)
                val endLocationAppModel: Location? = Location.from(activityLocationDataModel = endLocation)

                if (startLocationAppModel == null || endLocationAppModel == null) {
                    return null
                }

                // Convert to enum
                val activityTypeEnum = activityType?.let { activityType ->
                    try {
                        ActivityType.valueOf(activityType)
                    } catch (e: IllegalArgumentException) {
                        //    statusLog = "⚠️ Unknown activity type: $activityType"
                        ActivityType.UNKNOWN_ACTIVITY_TYPE
                    }
                } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE


                return ActivitySegment(
                    activities = activities?.mapNotNull { activity ->
                        activity.activityType?.let { activityType ->
                            val activitiesTypeEnum = try {
                                ActivityType.valueOf(activityType)
                            } catch (e: IllegalArgumentException) {
                                ActivityType.UNKNOWN_ACTIVITY_TYPE
                            }

                            Activity(
                                activityType = activitiesTypeEnum,
                                rawActivityType = activityType
                            )
                        }
                    } ?: emptyList(),
                    activityType = activityTypeEnum,
                    rawActivityType = activityType,
                    distance = distance ?: (waypointPath?.distanceMeters)?.toInt() ?: 0,
                    durationEndTimestamp = duration.endTimestamp,
                    durationStartTimestamp = duration.startTimestamp,
                    endLocation = endLocationAppModel,
                    startLocation = startLocationAppModel,
                    waypointPath = waypointPath?.let { WaypointPath.from(waypointPathDataModel = it) },
                    lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
                    eventTimeZone = timeZoneMap.getOverlappingTimeZone(
                        degreesLatitude = endLocationAppModel.latitudeE7 * 0.0000001,
                        degreesLongitude = endLocationAppModel.longitudeE7 * 0.0000001,
                    )
                )
            }
        }
    }

    private val mileageFormat = DecimalFormat("#,###.#")

    fun asTimelineItem(
        shouldShowMiles: Boolean,
        firstPlaceDetails: PlaceDetails?,
        lastPlaceDetails: PlaceDetails?,
        startPlaceDetails: PlaceDetails?,
        endPlaceDetails: PlaceDetails?,
        eventTimeZone: TimeZone?
    ): TimelineItem {
        val distanceInKilometers: Double = distance / 1000.0
        val distanceString = if (shouldShowMiles)
            "${mileageFormat.format(kilometersToMiles(distanceInKilometers))}mi"
        else
            "${mileageFormat.format(distanceInKilometers)}km"

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
            id = lastEditedTimestamp,
            placeId = endLocation.placeId, // Usually null
            subject = subject,
            location = endLocation.address ?: lastPlaceDetails?.formattedAddress ?: endLocation.getFormattedLatLng()
            ?: "Unknown",
            startTimeStamp = durationStartTimestamp,
            endTimeStamp = durationEndTimestamp,
            lastEditTimeStamp = lastEditedTimestamp,
            eventLatLng = LatLng(
                latitude = endLocation.getLatitude(),
                longitude = endLocation.getLongitude()
            ),
            eventTimeZone = eventTimeZone,
            placeUrl = endLocation.placeId?.let
            { endLocation.getGoogleMapsPlaceIdLink() }
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
}


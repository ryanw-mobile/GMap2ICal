/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

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
                val activityTypeEnum = resolveActivityType(activityType)

                return ActivitySegment(
                    activities = activities?.mapNotNull { activity ->
                        activity.activityType?.let {
                            Activity(
                                activityType = resolveActivityType(it),
                                rawActivityType = it
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

        private fun resolveActivityType(activityType: String?): ActivityType {
            return activityType?.let {
                try {
                    ActivityType.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    ActivityType.UNKNOWN_ACTIVITY_TYPE
                }
            } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE
        }
    }
}


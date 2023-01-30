/*
 * Copyright (c) 2023. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.models.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper

fun uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegment.toDomainModel(
    timeZoneMap: TimeZoneMapWrapper,
): ActivitySegment? {
    val startLocationAppModel: Location? = Location.from(activityLocationDataModel = startLocation)
    val endLocationAppModel: Location? = Location.from(activityLocationDataModel = endLocation)

    if (startLocationAppModel == null || endLocationAppModel == null) {
        return null
    }

    val activityTypeEnum = ActivityType.parse(activityType)
    val timezone = timeZoneMap.getOverlappingTimeZone(
        degreesLatitude = endLocationAppModel.latitudeE7 * 0.0000001,
        degreesLongitude = endLocationAppModel.longitudeE7 * 0.0000001,
    )

    return ActivitySegment(
        activities = activities?.mapNotNull { activity ->
            activity.activityType?.let { activityType ->
                Activity(
                    activityType = ActivityType.parse(activityType),
                    rawActivityType = activityType
                )
            }
        } ?: emptyList(),
        activityType = activityTypeEnum,
        rawActivityType = activityType,
        distance = distance ?: (waypointPath?.distanceMeters)?.toInt() ?: 0,
        durationEndTimestamp = RawTimestamp(
            timestamp = duration.endTimestamp,
            timezoneId = timezone?.zoneId ?: "UTC"
        ),
        durationStartTimestamp = RawTimestamp(
            timestamp = duration.startTimestamp,
            timezoneId = timezone?.zoneId ?: "UTC"
        ),
        endLocation = endLocationAppModel,
        startLocation = startLocationAppModel,
        waypointPath = waypointPath?.let { WaypointPath.from(waypointPathDataModel = it) },
        lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
        eventTimeZone = timezone
    )
}

/*
 * Copyright (c) 2023. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.models.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.toDomainModel
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper

fun uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegment.toDomainModel(
    timeZoneMap: TimeZoneMapWrapper,
): ActivitySegment? {
    val startLocationDomainModel: Location? = startLocation.toDomainModel()
    val endLocationDomainModel: Location? = endLocation.toDomainModel()

    if (startLocationDomainModel == null || endLocationDomainModel == null) {
        return null
    }

    val activityTypeEnum = ActivityType.parse(activityType)
    val timezone = timeZoneMap.getOverlappingTimeZone(
        degreesLatitude = endLocationDomainModel.latitudeE7 * 0.0000001,
        degreesLongitude = endLocationDomainModel.longitudeE7 * 0.0000001,
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
        endLocation = endLocationDomainModel,
        startLocation = startLocationDomainModel,
        waypointPath = waypointPath?.toDomainModel(),
        lastEditedTimestamp = lastEditedTimestamp ?: duration.endTimestamp,
        eventTimeZone = timezone
    )
}

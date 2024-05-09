/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.data.models.timeline.ActivityDto
import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocationDto
import uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegmentDto
import uk.ryanwong.gmap2ics.data.models.timeline.DurationDto
import uk.ryanwong.gmap2ics.data.models.timeline.LocationDto
import uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisitDto
import uk.ryanwong.gmap2ics.data.models.timeline.RoadSegmentDto
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjectDto
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjectsDto
import uk.ryanwong.gmap2ics.data.models.timeline.WaypointPathDto
import uk.ryanwong.gmap2ics.domain.models.ActivityType.IN_PASSENGER_VEHICLE
import uk.ryanwong.gmap2ics.domain.models.ActivityType.STILL
import uk.ryanwong.gmap2ics.domain.models.ActivityType.WALKING
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import us.dustinj.timezonemap.TimeZone

internal object TimelineTestData {
    val timelineObjectsDto = TimelineObjectsDto(
        timelineObjects = listOf(
            TimelineObjectDto(
                activitySegment = ActivitySegmentDto(
                    activities = listOf(
                        ActivityDto(activityType = "IN_PASSENGER_VEHICLE", probability = 84.83055830001831),
                        ActivityDto(activityType = "STILL", probability = 11.161107569932938),
                        ActivityDto(activityType = "WALKING", probability = 3.317141532897949),
                    ),
                    activityType = "IN_PASSENGER_VEHICLE",
                    confidence = "HIGH",
                    distance = 2373,
                    duration = DurationDto(
                        endTimestamp = "2022-01-03T14:18:02Z",
                        startTimestamp = "2022-01-03T14:04:35.990Z",
                    ),
                    endLocation = ActivityLocationDto(
                        address = null,
                        latitudeE7 = 534774054,
                        locationConfidence = null,
                        longitudeE7 = -22663978,
                        name = null,
                        placeId = null,
                    ),
                    startLocation = ActivityLocationDto(
                        address = null,
                        latitudeE7 = 534756174,
                        locationConfidence = null,
                        longitudeE7 = -22940374,
                        name = null,
                        placeId = null,
                    ),
                    waypointPath = WaypointPathDto(
                        confidence = 0.9905998589035128,
                        distanceMeters = 3838.6799403449295,
                        roadSegment = listOf(
                            RoadSegmentDto(duration = "12s", placeId = "some-road-segment-place-id-1"),
                            RoadSegmentDto(duration = "8s", placeId = "some-road-segment-place-id-2"),
                            RoadSegmentDto(duration = "7s", placeId = "some-road-segment-place-id-3"),
                        ),
                        source = "INFERRED",
                        travelMode = "DRIVE",
                    ),
                    lastEditedTimestamp = null,
                    activityConfidence = null,
                ),
                placeVisit = null,
            ),
            TimelineObjectDto(
                activitySegment = null,
                placeVisit = PlaceVisitDto(
                    centerLatE7 = null,
                    centerLngE7 = null,
                    duration = DurationDto(endTimestamp = "2022-01-03T14:26:25Z", startTimestamp = "2022-01-03T14:18:02Z"),
                    lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                    location = LocationDto(
                        address = "some-place-visit-location-address",
                        latitudeE7 = 534781060,
                        locationConfidence = 100.0,
                        longitudeE7 = -22666767,
                        name = "some-place-visit-location-name",
                        placeId = "some-place-visit-location-place-id",
                    ),
                    locationAssertionType = "WITHIN_OR_AT",
                    locationConfidence = 100,
                    placeConfidence = "USER_CONFIRMED",
                    placeVisitType = "SINGLE_PLACE",
                    visitConfidence = 100,
                    childVisits = null,
                ),
            ),

            TimelineObjectDto(
                activitySegment = ActivitySegmentDto(
                    activities = listOf(
                        ActivityDto(activityType = "IN_PASSENGER_VEHICLE", probability = 96.32002711296082),
                        ActivityDto(activityType = "WALKING", probability = 2.1393459290266037),
                        ActivityDto(activityType = "STILL", probability = 1.161708775907755),
                    ),
                    activityType = "IN_PASSENGER_VEHICLE",
                    confidence = "HIGH",
                    distance = 2183,
                    duration = DurationDto(endTimestamp = "2022-01-03T14:32:08Z", startTimestamp = "2022-01-03T14:26:25Z"),
                    endLocation = ActivityLocationDto(
                        address = null,
                        latitudeE7 = 534723150,
                        locationConfidence = null,
                        longitudeE7 = -22399807,
                        name = null,
                        placeId = null,
                    ),
                    startLocation = ActivityLocationDto(
                        address = null,
                        latitudeE7 = 534775230,
                        locationConfidence = null,
                        longitudeE7 = -22661872,
                        name = null,
                        placeId = null,
                    ),
                    waypointPath = WaypointPathDto(
                        confidence = 0.9996660206177816,
                        distanceMeters = 2318.9909458116717,
                        roadSegment = listOf(
                            RoadSegmentDto(duration = "3s", placeId = "some-road-segment-place-id-1"),
                            RoadSegmentDto(duration = "2s", placeId = "some-road-segment-place-id-2"),
                            RoadSegmentDto(duration = "12s", placeId = "some-road-segment-place-id-3"),
                        ),
                        source = "INFERRED",
                        travelMode = "DRIVE",
                    ),
                    lastEditedTimestamp = null,
                    activityConfidence = null,
                ),
                placeVisit = PlaceVisitDto(
                    centerLatE7 = null,
                    centerLngE7 = null,
                    duration = DurationDto(
                        endTimestamp = "2022-01-03T14:51:33.608Z",
                        startTimestamp = "2022-01-03T14:32:08Z",
                    ),
                    lastEditedTimestamp = "2022-01-05T06:56:03.277Z",
                    location = LocationDto(
                        address = "some-place-visit-location-address",
                        latitudeE7 = 534730563,
                        locationConfidence = 100.0,
                        longitudeE7 = -22399568,
                        name = "some-place-visit-location-name",
                        placeId = "some-place-visit-location-place-id",
                    ),
                    locationAssertionType = "WITHIN_OR_AT",
                    locationConfidence = 100,
                    placeConfidence = "USER_CONFIRMED",
                    placeVisitType = "SINGLE_PLACE",
                    visitConfidence = 100,
                    childVisits = null,
                ),
            ),
        ),
    )

    val timeline = Timeline(
        timelineEntries = listOf(
            TimelineEntry(
                activitySegment = uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment(
                    activities = listOf(
                        uk.ryanwong.gmap2ics.domain.models.timeline.activity.Activity(
                            activityType = IN_PASSENGER_VEHICLE,
                            rawActivityType = "IN_PASSENGER_VEHICLE",
                        ),
                        uk.ryanwong.gmap2ics.domain.models.timeline.activity.Activity(
                            activityType = STILL,
                            rawActivityType = "STILL",
                        ),
                        uk.ryanwong.gmap2ics.domain.models.timeline.activity.Activity(
                            activityType = WALKING,
                            rawActivityType = "WALKING",
                        ),
                    ),
                    activityType = IN_PASSENGER_VEHICLE,
                    rawActivityType = "IN_PASSENGER_VEHICLE",
                    distance = 2373,
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:18:02Z",
                        timezoneId = "Europe/London",
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:04:35.990Z",
                        timezoneId = "Europe/London",
                    ),
                    endLocation = uk.ryanwong.gmap2ics.domain.models.timeline.Location(
                        placeId = null,
                        latitudeE7 = 534774054,
                        longitudeE7 = -22663978,
                        name = null,
                        address = null,
                    ),
                    startLocation = uk.ryanwong.gmap2ics.domain.models.timeline.Location(
                        placeId = null,
                        latitudeE7 = 534756174,
                        longitudeE7 = -22940374,
                        name = null,
                        address = null,
                    ),
                    waypointPath = uk.ryanwong.gmap2ics.domain.models.timeline.activity.WaypointPath(
                        distanceMeters = 3838.6799403449295,
                        roadSegmentPlaceIds = listOf(
                            "some-road-segment-place-id-1",
                            "some-road-segment-place-id-2",
                            "some-road-segment-place-id-3",
                        ),
                    ),
                    lastEditedTimestamp = "2022-01-03T14:18:02Z",
                    eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
                ),
                placeVisit = null,
            ),
            TimelineEntry(
                activitySegment = null,
                placeVisit = uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit(
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:26:25Z",
                        timezoneId = "Europe/London",
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:18:02Z",
                        timezoneId = "Europe/London",
                    ),
                    lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                    location = uk.ryanwong.gmap2ics.domain.models.timeline.Location(
                        placeId = "some-place-visit-location-place-id",
                        latitudeE7 = 534781060,
                        longitudeE7 = -22666767,
                        name = "some-place-visit-location-name",
                        address = "some-place-visit-location-address",
                    ),
                    childVisits = listOf(),
                    eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
                ),
            ),
            TimelineEntry(
                activitySegment = uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment(
                    activities = listOf(
                        uk.ryanwong.gmap2ics.domain.models.timeline.activity.Activity(
                            activityType = IN_PASSENGER_VEHICLE,
                            rawActivityType = "IN_PASSENGER_VEHICLE",
                        ),
                        uk.ryanwong.gmap2ics.domain.models.timeline.activity.Activity(
                            activityType = WALKING,
                            rawActivityType = "WALKING",
                        ),
                        uk.ryanwong.gmap2ics.domain.models.timeline.activity.Activity(
                            activityType = STILL,
                            rawActivityType = "STILL",
                        ),
                    ),
                    activityType = IN_PASSENGER_VEHICLE,
                    rawActivityType = "IN_PASSENGER_VEHICLE",
                    distance = 2183,
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:32:08Z",
                        timezoneId = "Europe/London",
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:26:25Z",
                        timezoneId = "Europe/London",
                    ),
                    endLocation = uk.ryanwong.gmap2ics.domain.models.timeline.Location(
                        placeId = null,
                        latitudeE7 = 534723150,
                        longitudeE7 = -22399807,
                        name = null,
                        address = null,
                    ),
                    startLocation = uk.ryanwong.gmap2ics.domain.models.timeline.Location(
                        placeId = null,
                        latitudeE7 = 534775230,
                        longitudeE7 = -22661872,
                        name = null,
                        address = null,
                    ),
                    waypointPath = uk.ryanwong.gmap2ics.domain.models.timeline.activity.WaypointPath(
                        distanceMeters = 2318.9909458116717,
                        roadSegmentPlaceIds = listOf(
                            "some-road-segment-place-id-1",
                            "some-road-segment-place-id-2",
                            "some-road-segment-place-id-3",
                        ),
                    ),
                    lastEditedTimestamp = "2022-01-03T14:32:08Z",
                    eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
                ),
                placeVisit = uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit(
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:51:33.608Z",
                        timezoneId = "Europe/London",
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2022-01-03T14:32:08Z",
                        timezoneId = "Europe/London",
                    ),
                    lastEditedTimestamp = "2022-01-05T06:56:03.277Z",
                    location = uk.ryanwong.gmap2ics.domain.models.timeline.Location(
                        placeId = "some-place-visit-location-place-id",
                        latitudeE7 = 534730563,
                        longitudeE7 = -22399568,
                        name = "some-place-visit-location-name",
                        address = "some-place-visit-location-address",
                    ),
                    childVisits = listOf(),
                    eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
                ),
            ),
        ),
    )
}

/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

internal object ActivitySegmentDtoTestData {
    val activitySegmentDto = ActivitySegmentDto(
        activities = listOf(
            ActivityDto(
                activityType = "IN_PASSENGER_VEHICLE",
                probability = 99.82574267034934,
            ),
            ActivityDto(
                activityType = "WALKING",
                probability = 0.09030052981224143,
            ),
            ActivityDto(
                activityType = "MOTORCYCLING",
                probability = 0.04459950826743197,
            ),
        ),
        activityType = "IN_PASSENGER_VEHICLE",
        confidence = "HIGH",
        distance = 15032,
        duration = DurationDto(
            endTimestamp = "2019-06-01T01:24:28Z",
            startTimestamp = "2019-06-01T01:04:01Z",
        ),
        endLocation = ActivityLocationDto(
            address = null,
            latitudeE7 = 344643393,
            locationConfidence = null,
            longitudeE7 = 1324226167,
            name = null,
            placeId = null,
        ),
        startLocation = ActivityLocationDto(
            address = null,
            latitudeE7 = 343970563,
            locationConfidence = null,
            longitudeE7 = 1324677422,
            name = null,
            placeId = null,
        ),
        waypointPath = WaypointPathDto(
            confidence = null,
            distanceMeters = 15444.856340505617,
            roadSegment = listOf(
                RoadSegmentDto(duration = null, placeId = "some-place-id-1"),
                RoadSegmentDto(duration = null, placeId = "some-place-id-2"),
                RoadSegmentDto(duration = null, placeId = "some-place-id-3"),
            ),
            source = "INFERRED",
            travelMode = null,
        ),
        lastEditedTimestamp = null,
        activityConfidence = null,
    )
}

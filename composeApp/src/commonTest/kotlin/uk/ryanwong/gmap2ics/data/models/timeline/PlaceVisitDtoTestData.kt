/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

internal object PlaceVisitDtoTestData {
    val placeVisitDto = PlaceVisitDto(
        centerLatE7 = null,
        centerLngE7 = null,
        durationDto = DurationDto(endTimestamp = "2022-01-03T14:26:25Z", startTimestamp = "2022-01-03T14:18:02Z"),
        lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
        locationDto = LocationDto(
            address = "some-address",
            latitudeE7 = 534781060,
            locationConfidence = 100.0,
            longitudeE7 = -22666767,
            name = "some-name",
            placeId = "some-place-id",
        ),
        locationAssertionType = "WITHIN_OR_AT",
        locationConfidence = 100,
        placeConfidence = "USER_CONFIRMED",
        placeVisitType = "SINGLE_PLACE",
        visitConfidence = 100,
        childVisitDtos = listOf(
            ChildVisitDto(
                centerLatE7 = null,
                centerLngE7 = null,
                durationDto = DurationDto(endTimestamp = "2022-01-03T14:25:00Z", startTimestamp = "2022-01-03T14:19:00Z"),
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                locationDto = LocationDto(
                    address = "some-child-address",
                    latitudeE7 = 534781070,
                    locationConfidence = 100.0,
                    longitudeE7 = -22666770,
                    name = "some-child-name",
                    placeId = "some-child-place-id",
                ),
                locationConfidence = 100,
                placeConfidence = "USER_CONFIRMED",
                placeVisitType = "SINGLE_PLACE",
                visitConfidence = 100,
            ),
        ),
    )
}

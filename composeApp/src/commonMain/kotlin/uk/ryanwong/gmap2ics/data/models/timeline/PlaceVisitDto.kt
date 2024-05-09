/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class PlaceVisitDto(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val durationDto: DurationDto,
    val lastEditedTimestamp: String? = null,
    val locationDto: LocationDto,
    val locationAssertionType: String? = null,
    val locationConfidence: Int? = null,
    val placeConfidence: String? = null,
    val placeVisitType: String? = null,
    val visitConfidence: Int? = null,
    val childVisitDtos: List<ChildVisitDto>? = null,
)

/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class TimelineObjectDto(
    val activitySegmentDto: ActivitySegmentDto? = null,
    val placeVisitDto: PlaceVisitDto? = null,
)

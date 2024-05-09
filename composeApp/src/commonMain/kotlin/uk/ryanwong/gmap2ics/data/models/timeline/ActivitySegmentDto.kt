/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class ActivitySegmentDto(
    val activities: List<ActivityDto>? = null,
    val activityType: String? = null,
    val confidence: String? = null,
    val distance: Int? = null,
    val duration: DurationDto,
    val endLocation: ActivityLocationDto,
    val startLocation: ActivityLocationDto,
    val waypointPath: WaypointPathDto? = null,
    val lastEditedTimestamp: String? = null,
    val activityConfidence: Int? = null,
)

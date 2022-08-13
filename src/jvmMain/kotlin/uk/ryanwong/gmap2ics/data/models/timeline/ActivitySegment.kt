/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class ActivitySegment(
    val activities: List<Activity>? = null,
    val activityType: String? = null,
    val confidence: String? = null,
    val distance: Int? = null,
    val duration: Duration,
    val endLocation: ActivityLocation,
    val startLocation: ActivityLocation,
    val waypointPath: WaypointPath? = null,
    val lastEditedTimestamp: String? = null,
    val activityConfidence: Int? = null
)
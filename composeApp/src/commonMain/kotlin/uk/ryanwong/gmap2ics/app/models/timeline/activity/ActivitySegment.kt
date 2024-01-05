/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.models.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import us.dustinj.timezonemap.TimeZone

data class ActivitySegment(
    val activities: List<Activity>,
    val activityType: ActivityType,
    val rawActivityType: String?, // for debug use when resolved into an unknown type
    val distance: Int,
    val durationEndTimestamp: RawTimestamp,
    val durationStartTimestamp: RawTimestamp,
    val endLocation: Location,
    val startLocation: Location,
    val waypointPath: WaypointPath? = null,
    val lastEditedTimestamp: String,
    val eventTimeZone: TimeZone?,
)

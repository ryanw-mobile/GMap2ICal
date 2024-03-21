/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit

data class TimelineEntry(
    val activitySegment: ActivitySegment? = null,
    val placeVisit: PlaceVisit? = null,
)

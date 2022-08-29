/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class TimelineObject(
    val activitySegment: ActivitySegment? = null,
    val placeVisit: PlaceVisit? = null
)

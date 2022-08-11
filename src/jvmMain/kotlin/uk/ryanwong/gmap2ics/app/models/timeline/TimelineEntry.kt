/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

data class TimelineEntry(
    val activitySegment: ActivitySegment? = null,
    val placeVisit: PlaceVisit? = null
) {
    fun hasActivitySegment(): Boolean {
        return activitySegment != null
    }

    fun hasPlaceVisit(): Boolean {
        return placeVisit != null
    }
}

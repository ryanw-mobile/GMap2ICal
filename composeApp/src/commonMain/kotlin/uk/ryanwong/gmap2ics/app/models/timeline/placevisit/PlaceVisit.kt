/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import us.dustinj.timezonemap.TimeZone

data class PlaceVisit(
    val durationEndTimestamp: RawTimestamp,
    val durationStartTimestamp: RawTimestamp,
    val lastEditedTimestamp: String,
    val location: Location,
    val childVisits: List<ChildVisit> = emptyList(),
    val eventTimeZone: TimeZone?,
)

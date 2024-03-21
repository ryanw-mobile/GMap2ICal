/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.placevisit

import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import us.dustinj.timezonemap.TimeZone

/***
 * PlaceVisit and ChildVisit look pretty similar,
 * but I decided not to make them tightly coupled by applying inheritance.
 * Because by design even they look similar, nobody says they share the same behavior.
 *
 * Tightly coupling is a sin nowadays.
 */
data class ChildVisit(
    val durationEndTimestamp: RawTimestamp,
    val durationStartTimestamp: RawTimestamp,
    val lastEditedTimestamp: String,
    val location: Location,
    val eventTimeZone: TimeZone?,
)

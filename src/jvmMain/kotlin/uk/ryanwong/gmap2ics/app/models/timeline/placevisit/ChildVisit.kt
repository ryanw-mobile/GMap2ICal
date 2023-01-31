/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
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

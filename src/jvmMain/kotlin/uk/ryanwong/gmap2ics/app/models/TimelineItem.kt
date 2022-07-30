/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import us.dustinj.timezonemap.TimeZone

data class TimelineItem(
    val id: String,
    val placeId: String?,
    val subject: String,
    val location: String,
    val startTimeStamp: String,
    val endTimeStamp: String,
    val lastEditTimeStamp: String,
    val eventLatLng: LatLng,
    val eventTimeZone: TimeZone?,
    val placeUrl: String? = null,
    val description: String? = null
)
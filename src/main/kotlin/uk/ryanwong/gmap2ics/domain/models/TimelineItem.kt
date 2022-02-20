package uk.ryanwong.gmap2ics.domain.models

import us.dustinj.timezonemap.TimeZone

data class TimelineItem(
    val id: String,
    val placeId: String?,
    val subject: String,
    val location: String,
    val startTimeStamp: String,
    val endTimeStamp: String,
    val lastEditTimeStamp: String,
    val eventLatitude: Double,
    val eventLongitude: Double,
    val eventTimeZone: TimeZone?,
    val placeUrl: String? = null,
    val description: String? = null
)
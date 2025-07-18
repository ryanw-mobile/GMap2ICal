/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.utils.timezonemap

import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

class TimeZoneMapImpl(private val timeZoneMap: TimeZoneMap) : TimeZoneMapWrapper {

    override fun getOverlappingTimeZone(degreesLatitude: Double, degreesLongitude: Double): TimeZone? = timeZoneMap.getOverlappingTimeZone(
        degreesLatitude = degreesLatitude,
        degreesLongitude = degreesLongitude,
    )
}

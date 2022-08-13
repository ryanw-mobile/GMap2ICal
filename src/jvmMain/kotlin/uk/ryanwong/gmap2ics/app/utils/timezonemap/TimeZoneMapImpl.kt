/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap

import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

class TimeZoneMapImpl(private val timeZoneMap: TimeZoneMap) : TimeZoneMapWrapper {

    override fun getOverlappingTimeZone(degreesLatitude: Double, degreesLongitude: Double): TimeZone? {
        return timeZoneMap.getOverlappingTimeZone(
            degreesLatitude = degreesLatitude,
            degreesLongitude = degreesLongitude
        )
    }
}
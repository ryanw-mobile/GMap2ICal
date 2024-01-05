/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap

import us.dustinj.timezonemap.TimeZone

interface TimeZoneMapWrapper {
    fun getOverlappingTimeZone(degreesLatitude: Double, degreesLongitude: Double): TimeZone?
}
